package dev.lunarcoffee.blazelight.shared.language

class LocalizedStrings {
    lateinit var languageCode: String
    val language: Language by lazy { LanguageManager.toLanguage(languageCode) }

    lateinit var create: String
    lateinit var newThread: String
    lateinit var newPost: String
    lateinit var settings: String
    lateinit var save: String
    lateinit var discard: String
    lateinit var register: String
    lateinit var login: String

    lateinit var home: String
    lateinit var forums: String
    lateinit var tools: String

    lateinit var registrationHeading: String
    lateinit var loginHeading: String
    lateinit var email: String
    lateinit var username: String
    lateinit var password: String
    lateinit var retypePassword: String
    lateinit var timeZoneParen: String
    lateinit var languageParen: String

    lateinit var newCategoryHeading: String
    lateinit var name: String

    lateinit var dateTimeSep: String
    lateinit var startedBy: String
    lateinit var lastPostBy: String
    lateinit var on: String

    lateinit var thread: String
    lateinit var posts: String
    lateinit var joined: String

    lateinit var createForum: String
    lateinit var newForumHeading: String
    lateinit var topic: String

    lateinit var addThread: String
    lateinit var newThreadHeading: String
    lateinit var title: String
    lateinit var typeSomething: String

    lateinit var addPost: String
    lateinit var newPostHeading: String
    lateinit var threadIdFormat: String
}
