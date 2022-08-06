package pl.battleships.javaquarkusship.api;

import pl.battleships.javaquarkusship.api.dto.InfoDto;

import java.util.List;

public class InformationResource implements InformationApi{
    @Override
    public InfoDto info() {
        return new InfoDto().name("javaQuarkus").details(
                List.of("java","quarkus")
        );
    }
}
