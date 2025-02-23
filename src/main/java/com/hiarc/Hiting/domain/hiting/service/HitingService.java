import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.domain.hiting.repository.HitingRepository;
import com.hiarc.Hiting.domain.hiting.repository.SolvedRepository;
import com.hiarc.Hiting.domain.hiting.service.SolvedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HitingService {

    private final HitingRepository hitingRepository;
    private final SolvedRepository solvedRepository;
    private final StudentRepository studentRepository;
    private final SolvedService solvedService;

}