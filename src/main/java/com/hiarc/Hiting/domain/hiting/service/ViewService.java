package com.hiarc.Hiting.domain.hiting.service;

import com.hiarc.Hiting.domain.admin.service.DateService;
import com.hiarc.Hiting.global.enums.DefaultDate;
import org.springframework.data.util.Pair;
import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.repository.DateRepository;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.domain.hiting.dto.view.*;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.domain.hiting.entity.Streak;
import com.hiarc.Hiting.domain.hiting.repository.HitingRepository;
import com.hiarc.Hiting.domain.hiting.repository.StreakRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViewService {

    private final HitingRepository hitingRepository;
    private final StudentRepository studentsRepository;
    private final StreakService streakService;
    private final DateRepository dateRepository;
    private final StreakRepository streakRepository;
    private final DateService dateService;

    LocalDate defaultStart = DefaultDate.DEFAULT_START.getDateTime().toLocalDate();
    LocalDate defaultEnd = DefaultDate.DEFAULT_END.getDateTime().toLocalDate();


    public WrapStreakListDTO wrapStreakListData(){

        Optional<Date> dateOptional = dateRepository.findTopByOrderByIdAsc();
        Date date = dateOptional.orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        LocalDateTime start = date.getSeasonStart();
        LocalDate startDate = start.toLocalDate();
        LocalDateTime end = date.getSeasonEnd();
        LocalDate endDate = end.toLocalDate();

        boolean isSeason = dateService.isSeason(start, end);
        int seasonTotal = calculateSeasonTotal(startDate, endDate, isSeason);


        List<Streak> allStreaks = streakRepository.findAll();
        List<StreakResponseDTO> streakList = allStreaks.stream()
                .map(streak -> {
                    Students student = streak.getStudents();
                    LocalDate streakEnd = streak.getStreakEnd();
                    LocalDate streakStart = streak.getStreakStart();

                    int totalStreak = 0;
                    if (streakStart != defaultStart && streakEnd != defaultEnd) {
                        totalStreak = streakService.calculateDays(streakStart, streakEnd);
                    }

                    int seasonStreak = 0;
                    if (isSeason) {
                        if ( streakStart != defaultStart && streakEnd != defaultEnd ) {}
                        if( streakStart.isBefore(startDate)) {
                            seasonStreak = streakService.calculateDays(startDate, streak.getStreakEnd());
                        } else { seasonStreak = totalStreak; }
                    }
                    return new StreakResponseDTO(
                            student.getHandle(),
                            student.getTier_level(),
                            student.getDivNum(),
                            seasonStreak,
                            totalStreak,
                            streakStart
                    );
                })
                .sorted((dto1, dto2) -> Integer.compare(dto2.getTotalStreak(), dto1.getTotalStreak()))
                .collect(Collectors.toList());

        return new WrapStreakListDTO(seasonTotal, streakList);


    }

    @Transactional(readOnly = true)
    public List<RankingDTO> wrapDivRankData(int div){
        List<Students> students = studentsRepository.findByDivNum(div);
        if (students.isEmpty()) {
            throw new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        List<RankingDTO> response = students.stream()
                .map(student -> {
                    Hiting hiting = hitingRepository.findByStudents(student);
                    if (hiting == null) {
                        throw new GeneralException(ErrorStatus.HITING_NOT_FOUND);
                    }
                    boolean isEvent = (hiting.getEventHiting() != 0);
                    return new RankingDTO(
                            student.getHandle(),
                            student.getTier_level(),
                            isEvent,
                            hiting.getDailyHiting(),
                            hiting.getSeasonHiting()
                    );
                })
                .sorted(Comparator.comparingInt(RankingDTO::getTotalHiting).reversed())
                .collect(Collectors.toList());

        return response;
    }

    public int calculateSeasonTotal(LocalDate Start, LocalDate End, boolean isSeason){

        int seasonTotal;

        if (isSeason) {
            seasonTotal = streakService.calculateDays(Start, End);
        } else { seasonTotal = 0; }

        return seasonTotal;
    }

    @Transactional(readOnly = true)
    public HandleResponseDTO wrapByHandle(String handle) {
        Students student = studentsRepository.findByHandle(handle)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND));
        Hiting hiting = hitingRepository.findByStudents(student);
        Streak streak = streakRepository.findByStudents(student);

        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new GeneralException(ErrorStatus.DATE_NOT_FOUND));

        LocalDateTime start = date.getSeasonStart();
        LocalDateTime end = date.getSeasonEnd();
        LocalDate streakStart = streak.getStreakStart();
        LocalDate streakEnd = streak.getStreakEnd();


        boolean isSeason = dateService.isSeason(start, end);

        int seasonTotal = 0;
        if (isSeason) {
            seasonTotal = streakService.calculateDays(start.toLocalDate(), end.toLocalDate());
        }

        int totalStreak = 0;
        if (!Objects.equals(streakStart, defaultStart) && !Objects.equals(streakEnd, defaultEnd)) {
            totalStreak = streakService.calculateDays(streakStart, streakEnd);
        }

        int seasonStreak = 0;
        if (isSeason && streakStart != defaultStart && streakEnd != defaultEnd) {
            if (streakStart.isBefore(start.toLocalDate())) {
                seasonStreak = streakService.calculateDays(start.toLocalDate(), streakEnd);
            } else {
                seasonStreak = totalStreak;
            }
        }


        int rank = calculateRank(handle, student.getDivNum());

        return new HandleResponseDTO(
                student.getHandle(),
                student.getTier_level(),
                student.getDivNum(),
                rank,
                streakStart,
                totalStreak,
                seasonStreak,
                seasonTotal,
                hiting.getTotalHiting(),
                hiting.getSeasonHiting(),
                hiting.getDailyHiting()
        );

    }

    @Transactional(readOnly = true)
    public int calculateRank(String handle, int divNum) {

        List<Students> list = studentsRepository.findByDivNum(divNum);
        if (list.isEmpty()) {
            return 0;
        }

        List<Pair<String, Integer>> handlePairs = new ArrayList<>();

        for (Students student : list) {
            Hiting hiting = hitingRepository.findByStudents(student);
            int seasonHiting = (hiting != null) ? hiting.getSeasonHiting() : 0;

            handlePairs.add(Pair.of(student.getHandle(), seasonHiting));
        }

        handlePairs.sort((p1, p2) -> Integer.compare(p2.getSecond(), p1.getSecond()));

        for (int i = 0; i < handlePairs.size(); i++) {
            if (handlePairs.get(i).getFirst().equals(handle)) {
                return i + 1;
            }
        }

        return 0;
    }

    public WrapMainDTO wrapMainData(){

        Optional<Date> dateOptional = dateRepository.findTopByOrderByIdAsc();
        Date date = dateOptional.orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        LocalDateTime startEvent = date.getEventStart();
        LocalDateTime endEvent = date.getEventEnd();

        List<MainRankingDTO> Div1List = fiveDivRankData(1);
        List<MainRankingDTO> Div2List = fiveDivRankData(2);
        List<MainRankingDTO> Div3List = fiveDivRankData(3);

        List<StreakResponseDTO> StreakList = wrapStreakListData().getStreakList();
        if (StreakList.size() > 6) {
            StreakList = StreakList.subList(0, 6);
        }

        List<MainEventDTO> EventList;

        boolean isEvent = dateService.isEvent(startEvent, endEvent);

        if (isEvent) {
            EventList = eightEventData();
        } else { EventList = null; }


        return new WrapMainDTO(Div1List, Div2List, Div3List, StreakList, EventList);

    }

    public List<MainRankingDTO> fiveDivRankData(int div){
        List<Students> students = studentsRepository.findByDivNum(div);
        if (students.isEmpty()) {
            return Collections.emptyList();
        }

        List<MainRankingDTO> response = students.stream()
                .map(student -> {
                    Hiting hiting = hitingRepository.findByStudents(student);
                    if (hiting == null) {
                        throw new GeneralException(ErrorStatus.HITING_NOT_FOUND);
                    }
                    return new MainRankingDTO(
                            student.getHandle(),
                            hiting.getSeasonHiting(),
                            0,
                            student.getTier_level()
                    );
                })
                .sorted(Comparator.comparingInt(MainRankingDTO::getTotalHiting).reversed())
                .collect(Collectors.toList());

        for (int i = 0; i < response.size(); i++) {
            response.get(i).updateRank(i + 1);
        }

        if (response.size() > 5) {
            return response.subList(0, 5);
        } else {
            return response;
        }
    }



    public List<MainEventDTO> eightEventData(){
        List<Students> students = studentsRepository.findAll();

        List<MainEventDTO> response = students.stream()
                .map(student -> {
                    Hiting hiting = hitingRepository.findByStudents(student);
                    if (hiting == null) {
                        throw new GeneralException(ErrorStatus.HITING_NOT_FOUND);
                    }
                    if (hiting.getEventHiting()>0){
                        return new MainEventDTO(
                                student.getHandle(),
                                student.getTier_level(),
                                hiting.getEventHiting()
                        );
                    }
                    return null;

                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(MainEventDTO::getEventHiting).reversed())
                .collect(Collectors.toList());

        if (response.size() > 8) {
            return response.subList(0, 8);
        } else {
            return response;
        }

    }

}
