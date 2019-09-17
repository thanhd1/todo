package com.nal.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nal.core.enums.WorkStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WorkForm extends AbstractForm {

    @NotEmpty(message = "workName must be not empty")
    private String workName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startingDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endingDate;

    @Enumerated(EnumType.STRING)
    private WorkStatus status;

}
