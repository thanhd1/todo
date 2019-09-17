package com.nal.controller;

import com.nal.core.entity.WorkEntity;
import com.nal.exception.TodoException;
import com.nal.form.WorkForm;
import com.nal.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/works",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Scope("request")
public class WorkController extends AbstractController {

    @Autowired
    private WorkService workService;

    @GetMapping("/{id}")
    public WorkEntity findById(@PathVariable Long id) {
        return workService.findById(id);
    }

    @GetMapping
    public Page<WorkEntity> findAll(@RequestParam(value = "sort", defaultValue = "id") String sortBy,
                                    @RequestParam(value = "order", defaultValue = "desc") String order,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Sort sort = "asc".equals(order.toLowerCase()) ?  Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return workService.findAll(PageRequest.of(page, pageSize,sort));
    }

    @PostMapping
    public WorkEntity createNewWork(@RequestBody @Valid WorkForm workForm) throws TodoException {
        return workService.add(workForm);
    }

    @PutMapping("/{id}")
    public WorkEntity editWork(@PathVariable Long id, @RequestBody @Valid WorkForm workForm) throws TodoException {
        return workService.edit(id, workForm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteWork(@PathVariable Long id) throws TodoException {
        workService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

