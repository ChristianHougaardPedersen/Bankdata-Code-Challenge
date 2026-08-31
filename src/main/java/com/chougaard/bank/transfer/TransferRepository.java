package com.chougaard.bank.transfer;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransferRepository implements PanacheRepository<Transfer> {

}
