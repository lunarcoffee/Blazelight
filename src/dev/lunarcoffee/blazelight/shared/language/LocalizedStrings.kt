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
    lateinit var myProfile: String
    lateinit var logOut: String

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
    lateinit var newForum: String

    lateinit var startedBy: String
    lateinit var lastPostBy: String
    lateinit var timeOn: String

    lateinit var thread: String
    lateinit var posts: String
    lateinit var joined: String
    lateinit var said: String

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
    lateinit var post: String

    lateinit var first: String
    lateinit var prev: String
    lateinit var of: String
    lateinit var next: String
    lateinit var last: String

    lateinit var forbidden: String
    lateinit var forbiddenHeading: String
    lateinit var forbiddenNotice: String
    lateinit var internalServerError: String
    lateinit var internalServerErrorHeading: String
    lateinit var internalServerErrorNotice: String
    lateinit var notFound: String
    lateinit var notFoundHeading: String
    lateinit var notFoundNotice: String

    lateinit var users: String
    lateinit var unsetParen: String

    lateinit var noThreadsInForum: String
    lateinit var noCategories: String
    lateinit var noForumsInCategory: String

    lateinit var invalidTitle1To300: String
    lateinit var invalidContent1To10000: String
    lateinit var invalidName1To100: String
    lateinit var invalidTopic1To1000: String
    lateinit var invalidEmail: String
    lateinit var invalidUsername1To40: String
    lateinit var invalidPassword8To1000: String

    lateinit var noPermissions: String
    lateinit var successForum: String
    lateinit var invalidUsernameOrPassword: String
    lateinit var loginToContinue: String
    lateinit var emailTaken: String
    lateinit var usernameTaken: String
    lateinit var passwordConfirmFail: String
    lateinit var successRegister: String
}
