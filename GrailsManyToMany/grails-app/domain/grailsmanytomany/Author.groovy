package grailsmanytomany

class Author {
    String name

    static hasMany = [books:Book]

    static constraints = {
      books nullable:true
    }
}
