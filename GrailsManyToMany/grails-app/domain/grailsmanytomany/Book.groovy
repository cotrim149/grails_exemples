package grailsmanytomany

class Book {
    String title
    String editor

    static belongsTo = Author
    static hasMany=[authors:Author]

    static constraints = {
    }
}
