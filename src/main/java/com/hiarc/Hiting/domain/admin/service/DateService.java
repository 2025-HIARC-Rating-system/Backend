package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.DateDTO;
import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.repository.DateRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DateService {

    private DateRepository dateRepository;

    @Autowired
    public DateService(DateRepository dateRepository) {
        this.dateRepository = dateRepository;
    }

    public void initialSeasonDate(DateDTO request) {
        validateDateRange(request);
        dateRepository.save(request.toEntitySeason());
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

        date.updateSeasonStart((request.getStart()));
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
}
