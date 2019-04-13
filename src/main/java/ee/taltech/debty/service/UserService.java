package ee.taltech.debty.service;

import ee.taltech.debty.entity.BankAccount;
import ee.taltech.debty.entity.Person;
import ee.taltech.debty.model.PersonDto;
import ee.taltech.debty.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    void setParamsFromDto(Person person, PersonDto personDto) {
        if (!getUserByEmail(personDto.getEmail()).isPresent()) {
            person.setEmail(personDto.getEmail());
        }
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        if (personDto.getPassword() != null && personDto.getPasswordConfirmation() != null) {
            person.setPassword(passwordEncoder.encode(personDto.getPasswordConfirmation()));
        }
    }

    Person toUser(PersonDto personDto) {
        Person person = new Person();
        this.setParamsFromDto(person, personDto);
        return person;
    }

    public Person saveNewUser(PersonDto personDto) {
        Person person = this.toUser(personDto);
        personRepository.save(person);
        return person;
    }

    void saveUser(Person person) {
        person.setModifiedAt(LocalDateTime.now());
        personRepository.save(person);
    }

    public Optional<Person> getUserByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public List<Person> getAllUsers() {
        return personRepository.findAll();
    }

    public Optional<Person> getUserById(Long id) {
        return personRepository.findById(id);
    }

    public boolean emailExists(String email) {
        return personRepository.findByEmail(email).isPresent();
    }

    public void addBankAccountForUser(BankAccount bankAccount, Long personId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (!personOptional.isPresent()) return;
        Person person = personOptional.get();

        bankAccount.setModified(LocalDateTime.now());

        person.setModifiedAt(LocalDateTime.now());
        person.setBankAccount(bankAccount);
        personRepository.save(person);
    }

    public Person updateUser(PersonDto personDto) {
        Optional<Person> personOptional = getUserById(personDto.getId());
        if (!personOptional.isPresent()) return null;

        Person person = personOptional.get();
        setParamsFromDto(person, personDto);
        return personRepository.save(person);
    }
}
