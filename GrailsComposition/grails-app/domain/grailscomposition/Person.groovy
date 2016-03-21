package grailscomposition

class Person {
    Address homeAddress
    Address workAddress
    static embedded = ['homeAddress','workAddress']
    static constraints = {
    }
}

class Address{
    String number
    String code
}
