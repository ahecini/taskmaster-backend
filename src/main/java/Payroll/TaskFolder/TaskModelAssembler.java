package Payroll.TaskFolder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TaskModelAssembler implements RepresentationModelAssembler<Task, EntityModel<Task>> {

  @Override
  public EntityModel<Task> toModel(Task task) {

    // Unconditional links to single-item resource and aggregate root

    EntityModel<Task> taskModel = EntityModel.of(task,
        linkTo(methodOn(TaskController.class).one(task.getId())).withSelfRel(),
        linkTo(methodOn(TaskController.class).all()).withRel("tasks"));

    // Conditional links based on state of the task

    if (task.getStatus() == TaskStatus.IN_PROGRESS) {
      taskModel.add(linkTo(methodOn(TaskController.class).cancel(task.getId())).withRel("cancel"));
      taskModel.add(linkTo(methodOn(TaskController.class).complete(task.getId())).withRel("complete"));
    }

    return taskModel;
  }
}
