package Payroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import Payroll.EmployeeFolder.*;
import Payroll.OrderFolder.*;
import Payroll.TaskFolder.*;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  //@Bean
  CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository, TaskRepository taskRepository) {

    return args -> {
      log.info("Preloading " + employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar")));
      log.info("Preloading " + employeeRepository.save(new Employee("Frodo", "Baggins", "thief")));
      employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));

      
      orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
      orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

      orderRepository.findAll().forEach(order -> {
        log.info("Preloaded " + order);
      });

      taskRepository.save(new Task("Learn german", "COMPLETED"));
      taskRepository.save(new Task("Read Jurassic Park", "IN_PROGRESS"));

      taskRepository.findAll().forEach(task -> {
        log.info("Preloaded " + task);
      });
    };
  }
}