package ssotom.clone.reddit.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ssotom.clone.reddit.demo.model.Subreddit;
import ssotom.clone.reddit.demo.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubredditDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private String description;

    public Subreddit mapToEntity(User user) {
        Subreddit subreddit = new Subreddit();
        subreddit.setName(name);
        subreddit.setDescription(description);
        subreddit.setUser(user);
        return subreddit;
    }

}
