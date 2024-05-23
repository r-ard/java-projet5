package net.safety.alerts.safetynet.services;

import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.repositories.MedicalRecordRepository;
import net.safety.alerts.safetynet.utils.AbstractService;
import net.safety.alerts.safetynet.utils.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService extends AbstractService<MedicalRecordEntity> {
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Override
    protected IRepository<MedicalRecordEntity> getRepository() {
        return this.medicalRecordRepository;
    }

    public MedicalRecordService() {
        super();
    }

    public MedicalRecordEntity getByName(String firstName, String lastName) {
        List<MedicalRecordEntity> medicalRecords = this.getAll();

        for(MedicalRecordEntity e : medicalRecords) {
            if(e.getFirstName().toLowerCase().equals(firstName) && e.getLastName().toLowerCase().equals(lastName))
                return e;
        }

        return null;
    }
}
