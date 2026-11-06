package com.manuel.gym_api;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Routine;
import com.manuel.gym_api.model.RoutineExercise;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.repository.ExerciseRepository;
import com.manuel.gym_api.repository.RoutineRepository;
import com.manuel.gym_api.repository.UserRepository;
import com.manuel.gym_api.service.ExerciseImportService;

@Component
public class DataInitializer implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	private final ExerciseImportService exerciseImportService;
	private final UserRepository userRepository;
	private final RoutineRepository routineRepository;
	private final ExerciseRepository exerciseRepository;
	private final PasswordEncoder passwordEncoder;

	public DataInitializer(ExerciseImportService exerciseImportService,
			UserRepository userRepository,
			RoutineRepository routineRepository,
			ExerciseRepository exerciseRepository,
			PasswordEncoder passwordEncoder) {
		this.exerciseImportService = exerciseImportService;
		this.userRepository = userRepository;
		this.routineRepository = routineRepository;
		this.exerciseRepository = exerciseRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		exerciseImportService.importExercises();
		initDefaultUser();
	}

	private void initDefaultUser() {
		String defaultUsername = "demo";
		String defaultEmail = "demo@gym.com";

		if (!userRepository.existsByUsername(defaultUsername) && !userRepository.existsByEmail(defaultEmail)) {
			log.info("Creando usuario por defecto para pruebas: {}", defaultUsername);
			User demoUser = new User();
			demoUser.setUsername(defaultUsername);
			demoUser.setEmail(defaultEmail);
			demoUser.setPassword(passwordEncoder.encode("Password123!"));
			demoUser.setFirstName("Usuario");
			demoUser.setLastName("Demo");
			demoUser.setDateOfBirth(LocalDate.of(2000, 1, 1));
			demoUser.setLanguagePreference("es");
			demoUser.setPremium(true);
			demoUser.setPublicProfile(true);

			demoUser = userRepository.save(demoUser);

			// Crear una rutina de prueba inicial para el usuario demo
			List<Exercise> exercises = exerciseRepository.findAll();
			if (!exercises.isEmpty()) {
				Routine routine = new Routine();
				routine.setName("Rutina Fuerza Demo");
				routine.setDescription("Rutina básica de prueba para tren superior y tren inferior.");
				routine.setUser(demoUser);

				int order = 1;
				for (Exercise ex : exercises) {
					if (order > 3) break;
					RoutineExercise re = new RoutineExercise();
					re.setRoutine(routine);
					re.setExercise(ex);
					re.setOrderIndex(order);
					re.setSets(3);
					re.setReps(10);
					re.setRestSeconds(90);
					re.setTargetWeight(50.0);
					re.setNotes("3 series de 10 reps con peso moderado");
					routine.getRoutineExercises().add(re);
					order++;
				}
				routineRepository.save(routine);
				log.info("Rutina de prueba creada para el usuario demo.");
			}

			log.info("Usuario por defecto creado exitosamente (username: {}, password: Password123!)", defaultUsername);
		}
	}
}
