package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.DateDTO;
import com.hiarc.Hiting.domain.admin.dto.EventDTO;
import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.repository.DateRepository;
import com.hiarc.Hiting.domain.hiting.repository.HitingRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import com.hiarc.Hiting.global.enums.DefaultDate;
import com.hiarc.Hiting.global.enums.EventCategory;
import com.hiarc.Hiting.global.enums.TagCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DateService {

    private final AdminService adminService;
    private final DateRepository dateRepository;
    private final HitingRepository hitingRepository;

    LocalDateTime defaultStart = DefaultDate.DEFAULT_START.getDateTime();
    LocalDateTime defaultEnd = DefaultDate.DEFAULT_END.getDateTime();


    @Transactional
    public void changeSeasonDate(DateDTO request) {
        validateDateRange(request.getStart(), request.getEnd());
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.updateSeasonStart(request.getStart());
        date.updateSeasonEnd(request.getEnd());

        dateRepository.save(date);
    }

    @Transactional
    public void changeEventAndDate(EventDTO request) {

        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();

        validateDateRange(start, end);
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.updateEventStart(start);
        date.updateEventEnd(end);

        String eventCategory = request.getEventCategory();
        String detailCategory = request.getDetailCategory();

        date.updateEventCategory(EventCategory.valueOf(eventCategory));

        if (Objects.equals(eventCategory, EventCategory.TAG_EVENT.toString())) {
            if (TagCategory.isValidEngTag(detailCategory)) {
                date.updateDetailCategory(detailCategory);
                adminService.initialAllStudentsTagCount();
            } else throw new NotFoundException(ErrorStatus.TAG_INVALID);
        } else {
            date.updateDetailCategory(detailCategory);
        }

        hitingRepository.resetEventHitingForAll();

    }

    @Transactional
    public void changeSeasonEndOnly(LocalDateTime end) {
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.updateSeasonEnd(end);

        dateRepository.save(date);
    }

    @Transactional
    public void changeEventEndOnly(LocalDateTime end) {
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.updateEventEnd(end);

        dateRepository.save(date);
    }

    //날짜 순서 검증
    public void validateDateRange(LocalDateTime start, LocalDateTime end) {

        if (start.isAfter(end)) {
            throw new GeneralException(ErrorStatus.INVALID_DATE_FORMAT);
        }
    }

    public boolean isSeason(LocalDateTime Start, LocalDateTime End) {


        LocalDateTime today = LocalDateTime.now();

        boolean isSeason = (!Objects.equals(Start, defaultStart) && !Objects.equals(End, defaultEnd))
                && !today.isBefore(Start) && !today.isAfter(End);

        return isSeason;
    }

    public boolean isEvent(LocalDateTime Start, LocalDateTime End) {


        LocalDateTime today = LocalDateTime.now();

        boolean isEvent = (!Objects.equals(Start, defaultStart) && !Objects.equals(End, defaultEnd))
                && !today.isBefore(Start) && !today.isAfter(End);

        return isEvent;
    }

}
