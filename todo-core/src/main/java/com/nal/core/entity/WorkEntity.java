package com.nal.core.entity;

import com.nal.core.enums.WorkStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "WORK")
public class WorkEntity extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "WORK_NAME", nullable = false, unique = true)
    private String workName;

    @Column(name = "STARTING_DATE")
    private LocalDateTime startingDate;

    @Column(name = "ENDING_DATE")
    private LocalDateTime endingDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private WorkStatus status;

}
