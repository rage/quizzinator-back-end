package app.domain;

import app.deserializers.UserJsonDeserializer;
import app.serializers.UserJsonSerializer;
import app.services.HashService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.data.domain.Persistable;

//TODO: this doesnt make much sense yet.
//hash is (was?) supposed to protect peoples answers

@Entity(name = "review_user")
@JsonIgnoreProperties(value = "new")
@JsonSerialize(using = UserJsonSerializer.class)
@JsonDeserialize(using = UserJsonDeserializer.class)
public class User implements Persistable<String> {
    @Column(unique = true)
    private String name;
    
    @Id
    @Column(name = "user_hash")
    private String hash;
    
    //determines the amount of reviews given to user
    //eg  reviewWeight=2, user gets twice as much reviews as other users
    @Column(nullable = false)
    private Integer reviewWeight = 1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.hash = HashService.HashUsername(name);
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return this.hash;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    public Integer getReviewWeight() {
        return reviewWeight;
    }

    public void setReviewWeight(Integer reviewWeight) {
        this.reviewWeight = reviewWeight;
    }
}
