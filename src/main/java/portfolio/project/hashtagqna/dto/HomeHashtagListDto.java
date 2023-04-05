package portfolio.project.hashtagqna.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeHashtagListDto {
    private List<HashtagDto> hashtagListDtoList;

    public HomeHashtagListDto() {
    }

    public HomeHashtagListDto(List<HashtagDto> hashtagListDtoList) {
        this.hashtagListDtoList = hashtagListDtoList;
    }
}
