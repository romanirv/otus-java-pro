package ru.otus.jpql;


import ru.otus.jpql.entities.Address;
import ru.otus.jpql.entities.Client;
import ru.otus.jpql.entities.Phone;
import ru.otus.jpql.repositories.ClientRepository;
import ru.otus.jpql.repositories.impl.ClientRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Application {
	public static void main(String[] args) {
		ClientRepository clientRepository = new ClientRepositoryImpl();

		Client newClient = new Client();
		newClient.setName("Client Test");

		Address newAddress = new Address();
		newAddress.setStreet("Address unknown");

		newClient.setAddress(newAddress);

		Phone newPhone = new Phone();
		newPhone.setNumber("+79892323391");
		newPhone.setClient(newClient);

		newClient.setPhones(Set.of(newPhone));

		clientRepository.createClient(newClient);

		List<Client> clientsList = clientRepository.findAll();
		clientsList.forEach(System.out::println);

		if (clientsList.size() > 0) {
			Optional<Client> foundClient = clientRepository.findById(clientsList.stream().findFirst().get().getId());
            foundClient.ifPresent(System.out::println);
		}
	}
}
