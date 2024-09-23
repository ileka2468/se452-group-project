package edu.depaul.cdm.se452.rfa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "messages", schema = "public")
public class Message {
    @Id
    @ColumnDefault("nextval('messages_message_id_seq')")
    @Column(name = "message_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "match_id", nullable = false)
    private RoommateMatch match;

    @Size(max = 500)
    @NotNull
    @Column(name = "content", nullable = false, length = 500)
    private String content;

}