package ma.emsi.hospital;

import ma.emsi.hospital.Service.IHospitalService;
import ma.emsi.hospital.entities.*;
import ma.emsi.hospital.repositories.MedecinRepository;
import ma.emsi.hospital.repositories.PatientRepository;
import ma.emsi.hospital.repositories.RendezVousRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class HospitalApplication{

	public static void main(String[] args) {
		SpringApplication.run(HospitalApplication.class, args);
	}

	@Bean
	CommandLineRunner start(IHospitalService iHospitalService,
							PatientRepository patientRepository,
							MedecinRepository medecinRepository,
							RendezVousRepository rendezVousRepository
							){
		return args -> {
			Stream.of("bader","aymane","ayoub","taha").forEach(name ->{
				Patient patient = new Patient();
				patient.setNom(name);
				patient.setDateNaissance(new Date());
				patient.setMalade(false);
				iHospitalService.savePatient(patient);
			});
			Stream.of("zakaria","yassine","yasmine","hamza").forEach(name ->{
				Medecin medecin = new Medecin();
				medecin.setNom(name);
				medecin.setEmail(name+"@gmail.com");
				medecin.setSpecialite(Math.random()>0.5?"Cardio":"Dentiste");
				iHospitalService.saveMedecin(medecin);
			});

			Page<Patient> patients =patientRepository.findAll(PageRequest.of(0,5));
			System.out.println("Total pages: "+patients.getTotalPages());
			System.out.println("Total elements: "+ patients.getTotalElements());
			System.out.println("Num page: "+ patients.getNumber());
			List<Patient> content = patients.getContent();

			Page<Patient> byMalade = patientRepository.findByMalade(true,PageRequest.of(0,4));

			// List<Patient> patientList = patientRepository.chercherPatient("%h%", 40);

			byMalade.forEach(p->{
				System.out.println("_________Patient___________");
				System.out.println(p.getId());
				System.out.println(p.getNom());
				System.out.println(p.getScore());
				System.out.println(p.isMalade());
			});


			System.out.println("********** find BY id***********");
			Patient patient = patientRepository.findById(1L).orElse(null);
			if(patient != null ){
				System.out.println(patient.getNom());
				System.out.println(patient.isMalade());
			}

			patient.setScore(870);
			patientRepository.save(patient);

			patientRepository.deleteById(1L);




			Patient patient2 = patientRepository.findById(1L).orElse(null);
			Patient patient1 = patientRepository.findByNom("bader");

			Medecin medecin = medecinRepository.findByNom("zakaria");

			RendezVous rendezVous = new RendezVous();
			rendezVous.setDate(new Date());
			rendezVous.setStatus(StatusRDV.PENDING);
			rendezVous.setMedecin(medecin);
			rendezVous.setPatient(patient1);
			iHospitalService.saveRendezVous(rendezVous);

			RendezVous rendezVous1 = rendezVousRepository.findById(1L).orElse(null);

			Consultation consultation=new Consultation();
			consultation.setDateConsultation(new Date());
			consultation.setRendezVous(rendezVous1);
			consultation.setRapport("good work");
			iHospitalService.saveConsultation(consultation);

		};
	}
}
