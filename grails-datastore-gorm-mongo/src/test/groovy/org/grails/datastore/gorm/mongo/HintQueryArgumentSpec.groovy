package org.grails.datastore.gorm.mongo

import grails.gorm.tests.GormDatastoreSpec
import grails.gorm.CriteriaBuilder
import grails.gorm.tests.Person
import com.mongodb.MongoException
import org.springframework.data.mongodb.UncategorizedMongoDbException

/**
 *
 */
class HintQueryArgumentSpec extends GormDatastoreSpec{

     void "Test that hints work on criteria queries"() {
         when:"A criteria query is created with a hint"
            CriteriaBuilder c = Person.createCriteria()
            c.list {
                eq 'firstName', 'Bob'
                arguments hint:["firstName":1]
            }
         then:"The query contains the hint"

            c.query.@queryArguments == [hint:['firstName':1]]



         when:"A dynamic finder uses a hint"
             def results = Person.findAllByFirstName("Bob", [hint:"firstName"])
         then:"The hint is used"
             UncategorizedMongoDbException exception = thrown()
             exception.cause.message == 'bad hint'

         when:"A dynamic finder uses a hint"
             results = Person.findAllByFirstName("Bob", [hint:["firstName":1]])
         then:"The hint is used"
             results.size() == 0
     }
}
