package Payroll.TaskFolder;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TaskController {

  private final TaskRepository repository;
  private final TaskModelAssembler assembler;

  TaskController(TaskRepository repository, TaskModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }


  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/tasks")
  CollectionModel<EntityModel<Task>> all() {

    List<EntityModel<Task>> tasks = repository.findAll().stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(tasks, linkTo(methodOn(TaskController.class).all()).withSelfRel());
  }

  // Single item
  
  @GetMapping("/tasks/{id}")
  EntityModel<Task> one(@PathVariable Long id) {

    Task task = repository.findById(id) //
        .orElseThrow(() -> new TaskNotFoundException(id));

    return assembler.toModel(task);
  }

  @PostMapping("/tasks")
  ResponseEntity<?> newTask(@RequestBody Task newTask) {

    newTask.setStatus(TaskStatus.IN_PROGRESS);
    EntityModel<Task> entityModel = assembler.toModel(repository.save(newTask));

    return ResponseEntity //
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
        .body(entityModel);
  }

  @DeleteMapping("/tasks/{id}/cancel")
  ResponseEntity<?> cancel(@PathVariable Long id) {

    Task task = repository.findById(id) //
        .orElseThrow(() -> new TaskNotFoundException(id));

    if (task.getStatus() == TaskStatus.IN_PROGRESS) {
      task.setStatus(TaskStatus.CANCELLED);
      return ResponseEntity.ok(assembler.toModel(repository.save(task)));
    }

    return ResponseEntity //
        .status(HttpStatus.METHOD_NOT_ALLOWED) //
        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
        .body(Problem.create() //
            .withTitle("Method not allowed") //
            .withDetail("You can't cancel a task that is in the " + task.getStatus() + " status"));
  }

  @PutMapping("/tasks/{id}/complete")
  ResponseEntity<?> complete(@PathVariable Long id) {

    Task task = repository.findById(id) //
        .orElseThrow(() -> new TaskNotFoundException(id));

    if (task.getStatus() == TaskStatus.IN_PROGRESS) {
      task.setStatus(TaskStatus.COMPLETED);
      return ResponseEntity.ok(assembler.toModel(repository.save(task)));
    }

    return ResponseEntity //
        .status(HttpStatus.METHOD_NOT_ALLOWED) //
        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
        .body(Problem.create() //
            .withTitle("Method not allowed") //
            .withDetail("You can't complete atask that is in the " + task.getStatus() + " status"));
  }
}
