package dev.lunarcoffee.blazelight.shared.language

class LocalizedStrings {
    lateinit var languageCode: String
    val language: Language by lazy { LanguageManager.toLanguage(languageCode) }

    lateinit var apoS: String

    lateinit var create: String
    lateinit var newThread: String
    lateinit var newPost: String
    lateinit var settings: String
    lateinit var save: String
    lateinit var discard: String
    lateinit var deleteAccount: String
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
    lateinit var realName: String
    lateinit var description: String
    lateinit var password: String
    lateinit var retypePassword: String
    lateinit var timeZone: String
    lateinit var timeZoneParen: String
    lateinit var languageWord: String
    lateinit var languageParen: String

    lateinit var themeParen: String

    lateinit var deleteCategory: String
    lateinit var deleteCategoryTitle: String
    lateinit var deleteCategoryConfirmMessage: String

    lateinit var newCategoryHeading: String
    lateinit var name: String
    lateinit var newForum: String
    lateinit var deleteForum: String

    lateinit var deleteForumTitle: String
    lateinit var deleteForumConfirmMessage: String

    lateinit var deleteAccountTitle: String
    lateinit var deleteAccountConfirmMessage: String

    lateinit var startedBy: String
    lateinit var lastPostBy: String
    lateinit var timeOn: String

    lateinit var thread: String
    lateinit var threads: String
    lateinit var recentThreads: String
    lateinit var posts: String
    lateinit var recentPosts: String
    lateinit var joined: String
    lateinit var said: String
    lateinit var delete: String
    lateinit var forceDelete: String
    lateinit var deleteThread: String
    lateinit var forceDeleteThread: String
    lateinit var your: String

    lateinit var deletePostCap: String
    lateinit var forceDeletePostCap: String
    lateinit var deletePost: String
    lateinit var forceDeletePost: String
    lateinit var deleteConfirmMessage: String
    lateinit var cancel: String

    lateinit var deleteThreadTitle: String
    lateinit var forceDeleteThreadTitle: String
    lateinit var deleteThreadConfirmMessage: String

    lateinit var createForum: String
    lateinit var newForumHeading: String
    lateinit var topic: String

    lateinit var addThread: String
    lateinit var newThreadHeading: String
    lateinit var title: String
    lateinit var typeSomething: String

    lateinit var addPost: String
    lateinit var newPostHeading: String
    lateinit var entityIdFormat: String
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
    lateinit var noneParen: String

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
    lateinit var invalidRealName: String
    lateinit var invalidDescription: String

    lateinit var noPermissions: String
    lateinit var successForum: String
    lateinit var invalidUsernameOrPassword: String
    lateinit var loginToContinue: String
    lateinit var emailTaken: String
    lateinit var usernameTaken: String
    lateinit var passwordConfirmFail: String
    lateinit var successRegister: String
}
