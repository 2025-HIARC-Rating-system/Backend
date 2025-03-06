package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.DateDTO;
import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.repository.DateRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import com.hiarc.Hiting.global.enums.DefaultDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.LocalDateTime.of;

@Service
@RequiredArgsConstructor
public class DateService {

    private DateRepository dateRepository;
    LocalDateTime defaultStart = DefaultDate.DEFAULT_START.getDateTime();
    LocalDateTime defaultEnd = DefaultDate.DEFAULT_START.getDateTime();

    @Autowired
    public DateService(DateRepository dateRepository) {
        this.dateRepository = dateRepository;
    }

    @Transactional
    public void changeSeasonDate(DateDTO request) {
        validateDateRange(request);
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.updateSeasonStart(request.getStart());
        date.updateSeasonEnd(request.getEnd());

        dateRepository.save(date);
    }

    @Transactional
    public void changeEventDate(DateDTO request) {
        validateDateRange(request);
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.updateEventStart((request.getStart()));
        date.updateEventEnd(request.getEnd());

        dateRepository.save(date);
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
    public void validateDateRange(DateDTO request) {

        if (request.getStart().isAfter(request.getEnd())) {
            throw new GeneralException(ErrorStatus.INVALID_DATE_FORMAT);
        }
    }

    public boolean isSeason(LocalDateTime Start, LocalDateTime End) {


        LocalDateTime today = LocalDateTime.now();

        if (today.getHour() < 6) { today = today.minusDays(1); }

        boolean isSeason = (!Objects.equals(Start, defaultStart) && !Objects.equals(End, defaultEnd))
                && !today.isBefore(Start) && !today.isAfter(End);

        return isSeason;
    }

    public boolean isEvent(LocalDateTime Start, LocalDateTime End) {


        LocalDateTime today = LocalDateTime.now();

        if (today.getHour() < 6) { today = today.minusDays(1); }

        boolean isEvent = (!Objects.equals(Start, defaultStart) && !Objects.equals(End, defaultEnd))
                && !today.isBefore(Start) && !today.isAfter(End);

        return isEvent;
    }

}
