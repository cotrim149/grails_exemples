package grailsonetomany

class Car {
    String code

    // Este carro pertence a este dono
    static belongsTo = [owner:Owner]

    static constraints = {
    }
}
