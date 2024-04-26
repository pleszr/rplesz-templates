package com.territory.control;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.territory.entity.Company;
import com.territory.entity.Employee;
import com.territory.entity.Territory;
import com.territory.entity.CompanyUuid;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.fields;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Log4j2
@Repository
public class MongoDBClient {
    private MongoClient mongoClient;
    @Value("${database.mongo.uri}")
    private String mongoURI;
    @Value("${database.mongo.name}")
    private String mongoDatabaseName;

    MongoDBClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
    @PostConstruct
    public void init() {
        ConnectionString connectionString = new ConnectionString(mongoURI);
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        mongoClient = MongoClients.create(clientSettings);
        log.debug("MongoDBClient initialized");
    }
    public Optional<Employee> getEmployeeByWorkdayId(String workdayId) {
        MongoDatabase database = mongoClient.getDatabase(mongoDatabaseName);
        MongoCollection<Employee> collection = database.getCollection("implementations.employee",Employee.class);
        Bson projectionFields = fields(
                computed("uuid","$_id"),
                computed("employeeId","$EmployeeInformation.ID"),
                computed("writingNumber","$EmployeeInformation.PayrollEmpNumber"),
                computed("firstName","$EmployeeInformation.FirstName"),
                computed("middleInitial","$EmployeeInformation.MiddleInitial"),
                computed("lastName","$EmployeeInformation.LastName"),
                computed("emailAddress","$EmployeeInformation.Email"),
                computed("phone","$EmployeeInformation.PhoneNumber"),
                computed("nationalProducerNumber","$EmployeeInformation.NPN"),
                computed("residentState","$EmployeeInformation.State"),
                computed("employmentStatus","$EmployeeStatus.EmtpStatus"),
                computed("role","$EmployeeJobClass.JobClass"),
                computed("territoryRef","$EmployeeTerritory.TerritoryRef")

        );
        Employee employee = collection.find(eq("EmployeeInformation.WorkdayEmployeeid", workdayId))
                .projection(projectionFields)
                .first();
        log.debug("Database was queried for EmployeeInformation.WorkdayEmployeeid = " + workdayId
                + ". Result: " + employee);
        return Optional.ofNullable(employee);
    }
    public Optional<Document> getCompanyUuidByTerritoryUuid(UUID territoryUuid) {
        MongoDatabase database = mongoClient.getDatabase(mongoDatabaseName);
        MongoCollection<Document> collection = database.getCollection("implementations.territory");
        Bson projectionFields = fields(
                computed(
                        "companyUuid","$TerritoryService.ReportingTerritory.CompanyUuid"
                )
        );
        Document doc = collection.find(eq("_id", territoryUuid))
                .projection(projectionFields)
                .first();
        log.debug("Database was queried for TerritoryService.ReportingTerritory.CompanyUuid = " + territoryUuid
                + ". Result: " + doc);
        return Optional.ofNullable(doc);
    }
    public Optional<Company> getCompanyByUuid(UUID uuid) {
        MongoDatabase database = mongoClient.getDatabase(mongoDatabaseName);
        MongoCollection<Company> collection = database.getCollection("implementations.territory", Company.class);
        Bson projectionFields = fields(
                computed("uuid","$_id"),
                computed("code", new Document("$convert", new Document("input", "$TerritoryInformation.TerritoryNumber").append("to", "string"))),
                computed("type","$TerritoryService.TerritoryType"),
                computed("name","$TerritoryInformation.TerritoryName")
        );
        Company company = collection.find(eq("_id", uuid))
                .projection(projectionFields)
                .first();
        log.info("Database was queried for _id = " + uuid + ". Result: " + company);
        return Optional.ofNullable(company);
    }

    public List<CompanyUuid> getAllCompanyUuid() {
        MongoDatabase database = mongoClient.getDatabase(mongoDatabaseName);
        MongoCollection<CompanyUuid> collection = database.getCollection("implementations.territory", CompanyUuid.class);
        Bson projectionFields = fields(
                computed("uuid","$_id")
        );
        Iterable<CompanyUuid> iterable = collection.find(eq("TerritoryService.TerritoryType", "Company"))
                .projection(projectionFields);
        List<CompanyUuid> list = new ArrayList<>();
        for(CompanyUuid companyUuid : iterable) {
            list.add(companyUuid);
        }
        return list;
    }
    public List<Territory> getAllTerritoryByCompanyUuid(String uuid) {
        MongoDatabase database = mongoClient.getDatabase(mongoDatabaseName);
        MongoCollection<Territory> collection = database.getCollection("implementations.territory", Territory.class);
        List<Bson> pipeline = Arrays.asList(
                match(eq("TerritoryService.ReportingTerritory.CompanyUuid", uuid)),
                lookup("implementations.employee",
                        "EmployeeService.CurrentEmployee.EmployeeUuid",
                        "Uuid",
                        "employeeData"),
                project(
                        fields(
                                computed("uuid", "$_id"),
                                computed("code", new Document("$convert", new Document("input", "$TerritoryInformation.TerritoryNumber").append("to", "string"))),
                                computed("type", "$TerritoryService.TerritoryType"),
                                computed("currentEmployeeUuid", "$EmployeeService.CurrentEmployee.EmployeeUuid"),
                                computed("companyUuid", "$TerritoryService.ReportingTerritory.CompanyUuid"),
                                computed("regionUuid", "$TerritoryService.ReportingTerritory.RegionUuid"),
                                computed("districtUuid", "$TerritoryService.ReportingTerritory.DistrictUuid"),
                                computed("staffUuid", "$TerritoryService.ReportingTerritory.StaffUuid"),
                                computed("mailingAddressLine1","$TerritoryInformation.ContactDetails.MailingAddress.MailingAddressLine1"),
                                computed("mailingAddressLine2","$TerritoryInformation.ContactDetails.MailingAddress.MailingAddressLine2"),
                                computed("mailingAddressLine3","$TerritoryInformation.ContactDetails.MailingAddress.MailingAddressLine3"),
                                computed("city","$TerritoryInformation.ContactDetails.MailingAddress.City"),
                                computed("stateCodeFlexdata","$TerritoryInformation.ContactDetails.MailingAddress.StateCodeFlexdata"),
                                computed("zipCode", new Document("$convert", new Document("input", "$TerritoryInformation.ContactDetails.MailingAddress.ZipCode").append("to", "string"))),
                                computed("zipCodeExtension","$TerritoryInformation.ContactDetails.MailingAddress.ZipCodeExtension"),
                                computed("primaryPhone","$TerritoryInformation.ContactDetails.PhoneDetails.Primary.PrimaryPhone"),
                                computed("zipCode", new Document("$convert", new Document("input", "$TerritoryInformation.ContactDetails.MailingAddress.ZipCode").append("to", "string"))),
                                computed("employeeID", new Document("$convert", new Document("input", new Document("$arrayElemAt", Arrays.asList("$employeeData.EmployeeInformation.ID", 0))).append("to", "string"))),
                                computed("writingNumber", new Document("$arrayElemAt", Arrays.asList("$employeeData.EmployeeInformation.PayrollEmpNumber", 0))),
                                computed("firstName", new Document("$arrayElemAt", Arrays.asList("$employeeData.EmployeeInformation.FirstName", 0))),
                                computed("middleInitial", new Document("$arrayElemAt", Arrays.asList("$employeeData.EmployeeInformation.MiddleInitial", 0))),
                                computed("lastName", new Document("$arrayElemAt", Arrays.asList("$employeeData.EmployeeInformation.LastName", 0))),
                                computed("email", new Document("$arrayElemAt", Arrays.asList("$employeeData.EmployeeInformation.Email", 0)))

                        )
                )
        );
        AggregateIterable<Territory> iterable = collection.aggregate(pipeline);
        List<Territory> list = new ArrayList<>();
        iterable.into(list);
        log.info("Database was queried for TerritoryService.ReportingTerritory.CompanyUuid = " + uuid
                + ". Result size: " + list.size());
        for (Territory territory : list) {
            log.trace("Territory: " + territory);
        }
        return list;
    }
}


