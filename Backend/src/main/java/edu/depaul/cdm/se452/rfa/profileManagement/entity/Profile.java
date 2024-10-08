package edu.depaul.cdm.se452.rfa.profileManagement.entity;
import edu.depaul.cdm.se452.rfa.authentication.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;
import java.util.Map;
@Getter
@Setter
@Entity
@Table(name = "profiles", schema = "public")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profiles_id_gen")
    @SequenceGenerator(name = "profiles_id_gen", sequenceName = "profiles_profile_id_seq", allocationSize = 1)
    @Column(name = "profile_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "is_actively_looking", nullable = false)
    private Boolean isActivelyLooking = false;

    @Size(max = 500)
    @Column(name = "bio", length = 500)
    private String bio;

    @NotNull
    @Column(name = "characteristics", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> characteristics;

    @Size(max = 80)
    @NotNull
    @ColumnDefault("https://ui-avatars.com/api/?background=0D8ABC&color=fff")
    @Column(name = "pfp_image", nullable = false, length = 80)
    private String pfpImage;

}