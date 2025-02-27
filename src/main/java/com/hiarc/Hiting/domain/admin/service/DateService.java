package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.DateDTO;
import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.repository.DateRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class DateService {

    private DateRepository dateRepository;

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
    public void changeSeasonEndOnly(DateDTO request) {
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.updateSeasonEnd(request.getEnd());

        dateRepository.save(date);
    }

    @Transactional
    public void changeEventEndOnly(DateDTO request) {
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.updateEventEnd(request.getEnd());

        dateRepository.save(date);
    }

    //날짜 순서 검증
    public void validateDateRange(DateDTO request) {

        if (request.getStart().isAfter(request.getEnd())) {
            throw new GeneralException(ErrorStatus.INVALID_DATE_FORMAT);
        }
    }
}
