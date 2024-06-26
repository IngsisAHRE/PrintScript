package astbuilder

import utils.BooleanLiteral
import utils.Token

class BooleanLiteralBuilder(
    val tokens: List<Token>,
) : LiteralBuilder {
    override fun verifyAndBuild(): ASTBuilderResult =
        if (tokens.size == 1 && tokens[0].type == "BOOLEAN") {
            ASTBuilderSuccess(
                BooleanLiteral(
                    tokens[0].value.toBoolean(),
                    tokens[0].position.line,
                    tokens[0].position.start,
                    tokens[0].position.end,
                ),
            )
        } else {
            ASTBuilderFailure("Invalid boolean")
        }
}
