class IdentifierInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        require(node is Identifier) { "Node must be an Identifier" }
        val variableInfo = variableMap[node.name] ?: throw IllegalArgumentException("Variable not found")
        require(variableInfo.value != null) { "Variable value is null: ${node.name}" }
        return when {
            variableInfo.type == "string" -> variableInfo.value.toString()
            variableInfo.type == "number" -> variableInfo.value.toBigDecimal()
            variableInfo.type == "bool" &&
                VersionChecker().versionIsSameOrOlderThanCurrentVersion("1.1.0", version)
            -> variableInfo.value.toBoolean()
            else -> throw IllegalArgumentException("Unsupported variable type")
        }
    }
}
