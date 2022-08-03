package pl.battleships.javaspringship.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import pl.battleships.api.InformationApi;
import pl.battleships.api.dto.InfoDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class InformationController implements InformationApi {

    @Override
    public ResponseEntity<InfoDto> info() {
        return ResponseEntity.ok(new InfoDto().name("javaSpring").details(List.of("java", "spring")));
    }
}
