package honey.config.dsl

class LinkDSLBuilder(val parent: InstallDSLBuilder<*>) : ObjectWithFolder<LinkDSLBuilder> {
  override lateinit var folderPath: String

  override fun build(): LinkDSLBuilder {
    return this
  }
}