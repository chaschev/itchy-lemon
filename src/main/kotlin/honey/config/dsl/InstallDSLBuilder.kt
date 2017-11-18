package honey.config.dsl

import honey.config.AppConfig
import honey.config.StoredConfig

class InstallDSLBuilder<C: AppConfig> {
  var requiredVersions: RequireDSLBuilder? = null
  private var before: (() -> Unit)? = null
  private var after: (() -> Unit)? = null
  private var updateApp: (() -> Unit)? = null
  private var users: Users? = null
  private var rights: RightsDSL? = null
  private lateinit var folders: FoldersDSL

  var app: AppDSLBuilder? = null

  lateinit var config: StoredConfig<C>

  private lateinit var itemsInFolders: List<ObjectWithFolder<*>>

  val scripts = ArrayList<ScriptDSLBuilder>()
  val linkMakers = ArrayList<LinkDSLBuilder>()

  fun build(): InstallDSLBuilder<C> = this

  fun before(block: () -> Unit) {
    before = block
  }

  fun config(block: () -> StoredConfig<C>) {config = block()}

  fun require(builder: RequireDSLBuilder.() -> Unit) {
    requiredVersions = RequireDSLBuilder().apply(builder)
  }

  fun after(block: () -> Unit) {
    after = block
  }

  fun updateApp(block: () -> Unit) {
    updateApp = block
  }

  fun users() = users!!

  fun users(name: String) = users!![name]!!

  fun users(builder: UsersDSLBuilder.() -> Unit) {
    this.users = UsersDSLBuilder().apply(builder).build()
  }

  fun rights() = rights!!
  fun rights(name: String) = rights!![name]!!

  fun rights(builder: RightsDSLBuilder.() -> Unit) {
    this.rights = RightsDSLBuilder().apply(builder).build()
  }

  fun app() = app!!

  fun app(builder: AppDSLBuilder.() -> Unit) {
    this.app = AppDSLBuilder().apply(builder)
  }

  fun folders() = folders

  fun folders(name: String) = folders.map[name]!!

  fun folders(builder: FoldersDSLBuilder.() -> Unit) {
    this.folders = FoldersDSLBuilder(this).apply(builder).build()
  }

  fun inFolder(path: String, builder: InFoldersDSLBuilder.() -> Unit) {
    itemsInFolders = InFoldersDSLBuilder(this).folder(path).apply(builder).build()
  }

  fun inFolder(folder: Folder, builder: InFoldersDSLBuilder.() -> Unit)
    = inFolder(folder.path, builder)

  fun sortInFoldersOut(list: List<ObjectWithFolder<*>>) {
    list.forEach {
      when (it) {
        is ScriptDSLBuilder -> scripts.add(it)
        is LinkDSLBuilder -> linkMakers.add(it)
        else -> TODO()
      }
    }
  }
}
