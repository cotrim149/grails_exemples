package grailsonetomany

class Owner {
    String name

    // Este dono possui muitos carros
    static hasMany=[cars:Car]

    static constraints = {
    }

}
