import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.repository.DateRepository;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.domain.hiting.entity.Event;
import com.hiarc.Hiting.domain.hiting.dto.SolvedResponseDTO;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.domain.hiting.entity.Solved;
import com.hiarc.Hiting.domain.hiting.repository.HitingRepository;
import com.hiarc.Hiting.domain.hiting.repository.SolvedRepository;
import com.hiarc.Hiting.domain.hiting.service.SolvedAcService;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.hiarc.Hiting.global.enums.TierCategory.fromLeveltoTierRating;

@Service
@RequiredArgsConstructor
public class HitingService {

    private final HitingRepository hitingRepository;
    private final SolvedRepository solvedRepository;
    private final StudentRepository studentsRepository;
    private final SolvedAcService solvedAcService;
    private final DateRepository dateRepository;
    private final SolvedAcService.EventRepository eventRepository;


    //openapi에서 값 불러오기 / solved table에 저장 / hiting 값들 계산 및 저장
    // 15분에 한번씩 실행, 시간 검증으로 특정 시간은 적용 x
    //상관없이 호출하는 controller 하나 만들기
    @Transactional
    public void realTimeHitings() { //**handle 대신 학생 전체로 !!!

        Optional<Date> dateOptional = dateRepository.findTopByOrderByIdAsc();
        Date date = dateOptional.orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        LocalDateTime today = LocalDateTime.now();
        boolean isSeason = !today.isBefore(date.getSeasonStart()) && !today.isAfter(date.getSeasonEnd());
        boolean isEvent = !today.isBefore(date.getEventStart()) && !today.isAfter(date.getEventEnd());


        List<Students> allStudents = studentsRepository.findAll();
        for (Students student : allStudents) {

            //openAPI로 값 불러오기
            String handle = student.getHandle();
            List<SolvedResponseDTO> solvedList = solvedAcService.SolvedListByHandle(handle);
            //student에 대응하는 hiting 객체 불러오기
            Hiting hiting = hitingRepository.findByStudents(student);
            Event event = eventRepository.findByStudents(student);

            int delta = 0;

            for (SolvedResponseDTO dto : solvedList) {
                int level = dto.getLevel();
                int SolvedNow = dto.getSolved();
                int SolvedBefore;

                Optional<Solved> optional = solvedRepository.findByStudentsAndLevel(student, level); //값이 존재하지 않을수도있어서 Optional 사용

                if (optional.isEmpty()) {
                    SolvedBefore = 0;
                    solvedRepository.save(dto.toEntitySolved()); // solved DB값 최초 저장
                } else {
                    Solved solved = optional.get(); //컨테이너에서 꺼내기
                    SolvedBefore = solved.getEachSolved(); //이전에 있던 값 불러오기
                    solved.updateEachSolved(SolvedNow); //solved DB값 변경
                }

                //점수 자체를 계산하기 -> for문으로 다 더하기
                int tierRating = fromLeveltoTierRating(level);
                int levelDelta = (SolvedNow - SolvedBefore) * tierRating;
                delta += levelDelta;

            }

            if (isEvent){ //이벤트 기간 내인지 확인
                //calculateEventHiting -> 2배 이벤트 : delta 값을 그냥 2배 해줌
                //eventHiting 계산 : 이 함수 로직상 이벤트 기간 내이면 함수 부르기 / eventHiting 값 업데이트하기
                //이후에는 어떤이벤트인지 확인하는 로직 추가, 걔는 별개 함수로 구현
                delta = delta *2;
                event.addEventHiting(delta);
            }

            //daily 변경 : 얘는 오늘인지 확인할 이유가 없다. 00시에 초기화만 잘 해주면 됨
            //dailyHiting 계산 : 오늘 끝나면 초기화하기(특정 시간에만 작용하는 스케줄러 적용)(기타 함수로 구현)
            hiting.addDailyHiting(delta);

            if (isSeason){ //season기간인지 확인
                //season 종료 -> 시즌 종료, seasonHiting값 0으로 만든다(별도 api), recent db로 값 다 옮기기 /
                // 이외 기간 : recent 값 불러오기 (student으로), 현재 total 값이랑 거기 기준 total 값 차이를 season값으로 디비에 넣기 // season 시작(기간 입력) 하면 seasonHiting 0으로 다시 초기화 ->
                // 나머지 초기화는 새로운 시즌 시작되면 진행
                //season 값 변경
                //seasonHiting 계산 : 이 함수 로직상 시즌 기간 내이면 함수 부르기 / seasonHiting 값 업데이트하기
                calculateSeasonHiting
            }

            //total 변경
            //totalHiting 계산 : 원래 값에서 계속 추가하기
            calculateTotalHiting

            //streak반영
            //streak 함수 진행하기
            // -끝- 값 확인은 그냥 현재 모든 값 불러오는 controller 만들기
        }
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetDailyHiting(){
        List<Students> allStudents = studentsRepository.findAll();
        for (Students student : allStudents) {
            Hiting hiting = hitingRepository.findByStudents(student);
            hiting.updateDailyHiting(0);
        }
    }




}