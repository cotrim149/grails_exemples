package extraclass2

class Student {
    String universityNumber
    String name

    static hasMany = [courses:Course]

    static constraints = {
      courses nullable:true
    }
    String toString(){
      return this.name
    }
}
