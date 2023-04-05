package portfolio.project.hashtagqna.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HashtagListDto {
    private List<HashtagDto> hashtagDtoList = new ArrayList<>();

    public HashtagListDto() {
    }

    public HashtagListDto(List<HashtagDto> hashtagDtoList) {
        this.hashtagDtoList = hashtagDtoList;
    }
}
