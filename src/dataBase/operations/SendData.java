package dataBase.operations;

import entities.person.*;
import entities.properties.*;
import enumerations.PersonType;
import enumerations.Status;
import enumerations.TypeOfApart;
import enumerations.TypeOfProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static enumerations.PersonType.NATURAL_PERSON;
import static enumerations.TypeOfProperty.*;

public class SendData {
    public static void sendSqlAddContact(Connection connection, Contact contact, int registrationPersonNumber) {
        String contactID = contact.getId();
        String email = contact.getEmail();
        int phone = contact.getPhone();

        String sql = "INSERT INTO Contact VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, contactID);
            preparedStatement.setInt(2, registrationPersonNumber);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4, phone);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(STR."[sendSqlAddContact]: \{e.getMessage()}");
        }
    }
    public static void sendSqlAddAddress(Connection connection, Address address, int registrationPersonNumber) {
        String addressID = address.getId();
        String city = address.getCity();
        String zipCode = address.getZipCode();
        String district = address.getDistrict();
        String street = address.getStreet();
        int number = address.getNumber();

        String sql = "INSERT INTO PersonAddress VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, addressID);
            preparedStatement.setInt(2, registrationPersonNumber);
            preparedStatement.setString(3, city);
            preparedStatement.setString(4, zipCode);
            preparedStatement.setString(5, district);
            preparedStatement.setString(6, street);
            preparedStatement.setInt(7, number);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(STR."[sendSqlAddAddress]: \{e.getMessage()}");
        }
    }

    public static void sendSqlAddPropertyAddress(Connection connection, Address address, String propertyID) {
        String addressID = address.getId();
        String city = address.getCity();
        String zipCode = address.getZipCode();
        String district = address.getDistrict();

        String sql = "INSERT INTO PropertyAddress VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, addressID);
            preparedStatement.setString(2, propertyID);
            preparedStatement.setString(3, city);
            preparedStatement.setString(4, zipCode);
            preparedStatement.setString(5, district);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(STR."[sendSqlAddPropertyAddress]: \{e.getMessage()}");
        }
    }

    public static void sendSqlAddPerson(Connection connection, Person person) {
        String name = person.getPersonsName();
        String password = person.getPassword();
        PersonType personType = person.getPersonType();
        int registrationNumber = 0;

        switch (personType) {
            case NATURAL_PERSON -> {
                NaturalPerson naturalPerson = (NaturalPerson) person;
                registrationNumber = naturalPerson.getSsn();
            }
            case LEGAL_PERSON -> {
                LegalPerson legalPerson = (LegalPerson) person;
                registrationNumber = legalPerson.getEin();
            }
        }
        String sql = "INSERT INTO Person VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, registrationNumber);
            preparedStatement.setString(2, personType.toString());
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, password);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(STR."[sendSqlAddPerson]: \{e.getMessage()}");
        }
    }

    public static void sendAddPropertyOwner(Connection connection, Property property, Person person) {
        int registrationPersonNumber = -1;

        if(person.getPersonType().equals(NATURAL_PERSON)) {
            registrationPersonNumber = ((NaturalPerson) person).getSsn();
        } else {
            registrationPersonNumber = ((LegalPerson) person).getEin();
        }

        String propertyID = property.getId();
        String sql = "INSERT INTO PropertyOwner VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, propertyID);
            preparedStatement.setInt(2, registrationPersonNumber);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(STR."[sendAddPropertyOwner]: \{e.getMessage()}");
        }
    }

    public static void sendSqlAddProperty(Connection connection, Property property) {
        Status status = property.getStatus();
        String propertyID = property.getId();
        String propertyDesc = property.getDesc();
        double totalArea = property.getTotalArea();
        double price = property.getPrice();
        TypeOfProperty typeOfProperty = property.getTypeOfProperty();
        int floorOfTheApart = -1;
        String buildingName = null;
        int numberOfTheApart = -1;
        double condominiumValue = -1;
        TypeOfApart typeOfApart = null;
        int roomsNumber = -1;
        int yearBuilt = -1;
        double distanceOfCity = -1;
        double frontDimension = -1;
        double sideDimension = -1;
        int parkingSpaces = -1;
        int totalNumOfFloors = -1;
        double buildingArea = -1;

        if (typeOfProperty.equals(APARTMENT)) {
             floorOfTheApart = ((Apartment) property).getFloorOfTheApart();
             buildingName = ((Apartment) property).getBuildingName();
             numberOfTheApart = ((Apartment) property).getNumberOfTheApart();
             condominiumValue = ((Apartment) property).getCondominiumValue();
             typeOfApart = ((Apartment) property).getTypeOfApart();
             roomsNumber = ((Apartment) property).getTotalNumberOfRooms();
             yearBuilt = ((Apartment) property).getYearBuilt();
        } else if (typeOfProperty.equals(FARM)) {
            distanceOfCity = ((Farm) property).getDistanceOfCity();
            roomsNumber = ((Farm) property).getTotalNumberOfRooms();
            yearBuilt = ((Farm) property).getYearBuilt();
        } else if (property.getTypeOfProperty().equals(TypeOfProperty.LAND)) {
            frontDimension = ((Land) property).getFrontDimension();
            sideDimension = ((Land) property).getSideDimension();
        } else if (typeOfProperty.equals(HOUSE)) {
            parkingSpaces = ((House) property).getParkingSpaces();
            totalNumOfFloors = ((House) property).getNumbFloors();
            roomsNumber = ((House) property).getTotalNumberOfRooms();
            yearBuilt = ((House) property).getYearBuilt();
        }
        if (property.getTypeOfProperty().equals(FARM) || property.getTypeOfProperty().equals(HOUSE)) {
            buildingArea = ((House) property).getBuildingArea();
        }

        String sql = "INSERT INTO Property VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, propertyID);
            preparedStatement.setString(2, String.valueOf(typeOfProperty));
            preparedStatement.setString(3, propertyDesc);
            preparedStatement.setDouble(4, totalArea);
            preparedStatement.setDouble(5, price);
            preparedStatement.setString(6, String.valueOf(status));
            preparedStatement.setInt(7, floorOfTheApart);
            preparedStatement.setDouble(8, frontDimension);
            preparedStatement.setDouble(9, sideDimension);
            preparedStatement.setDouble(10, buildingArea);
            preparedStatement.setInt(11, numberOfTheApart);
            preparedStatement.setString(12, buildingName);
            preparedStatement.setInt(13, roomsNumber);
            preparedStatement.setInt(14, parkingSpaces);
            preparedStatement.setInt(15, yearBuilt);
            preparedStatement.setInt(16, totalNumOfFloors);
            preparedStatement.setDouble(17, condominiumValue);
            preparedStatement.setDouble(18, distanceOfCity);
            preparedStatement.setString(19, String.valueOf(typeOfApart));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(STR."[sendSqlAddProperty]: \{e.getMessage()}");
        }
    }

    public static void sendSqlRemoveContact(Connection connection, String propertyID) {
        String sql = STR."DELETE FROM Contact WHERE RegistrationPersonNumber = \{propertyID}";
        try {
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(STR."[sendSqlRemoveContact]: \{e.getMessage()}");
        }
    }

    public static void sendSqlRemovePropertyAddress(Connection connection, String propertyID) {
        String sql = STR."DELETE FROM PropertyAddress WHERE PropertyID = '\{propertyID}'";
        try {
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(STR."[sendSqlRemovePropertyAddress]: \{e.getMessage()}");
        }
    }

    public static void sendSqlRemovePropertyOwner(Connection connection, String propertyID) {
        String sql = STR."DELETE FROM PropertyOwner WHERE PropertyID = '\{propertyID}'";
        try {
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(STR."[sendSqlRemovePropertyOwner]: \{e.getMessage()}");
        }
    }

    public static void sendSqlRemoveProperty(Connection connection, String propertyID) {
        String sql = STR."DELETE FROM Property WHERE PropertyID = '\{propertyID}'";
        try {
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(STR."[sendSqlRemoveProperty]: \{e.getMessage()}");
        }
    }
}
