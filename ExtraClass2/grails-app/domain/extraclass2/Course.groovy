package extraclass2

class Course {
    String name
    String purpose

    static belongsTo = Student
    static hasOne = [teacher:Teacher]
    static hasMany = [students:Student]
    static constraints = {

    }
    String toString(){
      return this.name
    }
}
