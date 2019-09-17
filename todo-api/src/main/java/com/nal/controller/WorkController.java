package com.nal.controller;

import com.nal.core.entity.WorkEntity;
import com.nal.exception.TodoException;
import com.nal.form.WorkForm;
import com.nal.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/works",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Scope("request")
public class WorkController extends AbstractController {

    @Autowired
    private WorkService workService;

    @GetMapping
    public List<WorkEntity> findAll() {
        return workService.findAll();
    }

    @PostMapping
    public WorkEntity newWork(@RequestBody @Valid WorkForm workForm) throws TodoException {
        return workService.save(workForm);
    }

}

