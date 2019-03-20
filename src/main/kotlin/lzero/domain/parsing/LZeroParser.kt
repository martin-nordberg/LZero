//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.parsing

import lzero.domain.model.annotations.LZeroAnnotation
import lzero.domain.model.annotations.LZeroAnnotationList
import lzero.domain.model.arguments.*
import lzero.domain.model.connectedelements.LZeroConnectedElement
import lzero.domain.model.connectedelements.LZeroConnectedElementList
import lzero.domain.model.connectedelements.LZeroConnectedQualifiedName
import lzero.domain.model.connectedelements.LZeroConnectedUuid
import lzero.domain.model.connections.*
import lzero.domain.model.core.LZeroFileOrigin
import lzero.domain.model.documentation.LZeroBlockDocumentation
import lzero.domain.model.documentation.LZeroDocumentation
import lzero.domain.model.documentation.LZeroNullDocumentation
import lzero.domain.model.elements.LZeroConcept
import lzero.domain.model.elements.LZeroDeclaration
import lzero.domain.model.elements.LZeroElement
import lzero.domain.model.expressions.LZeroExpression
import lzero.domain.model.expressions.literals.*
import lzero.domain.model.expressions.references.LZeroReferenceExpression
import lzero.domain.model.names.LZeroName
import lzero.domain.model.names.LZeroNullName
import lzero.domain.model.names.LZeroQualifiedName
import lzero.domain.model.names.LZeroSimpleName
import lzero.domain.model.parameters.LZeroNullParameterList
import lzero.domain.model.parameters.LZeroParameter
import lzero.domain.model.parameters.LZeroParameterList
import lzero.domain.model.parameters.LZeroSpecifiedParameterList
import lzero.domain.model.uuids.LZeroKnownUuid
import lzero.domain.model.uuids.LZeroNullUuid
import lzero.domain.model.uuids.LZeroUuid
import lzero.domain.scanning.ELZeroTokenType.*
import lzero.domain.scanning.LZeroToken

//---------------------------------------------------------------------------------------------------------------------

class LZeroParser(
    private val fileName: String,
    code: String
) {

    private val input = LZeroExpectedTokenBufferImpl(code)

    ////

    /**
     * element
     *   : declaration
     *   : expression
     *   ;
     */
    fun parseElement(): LZeroElement {

        if (
            input.hasLookAhead(HASH) ||
            input.hasLookAhead(AT) ||
            input.hasLookAhead(2, HASH) ||
            input.hasLookAhead(2, AT)
        ) {
            // declaration
            return parseDeclaration()
        }

        // expression
        return parseExpression()

    }

    ////

    /**
     * Parses one annotation.
     *
     * annotation
     *   : "@" qualifiedName argumentList?
     *   ;
     */
    private fun parseAnnotation(): LZeroAnnotation {

        // "@"
        val atToken = input.read(AT)

        // qualifiedName
        val qualifiedName = parseQualifiedName()

        // parameterList?
        val argumentList = parseArgumentListOpt()

        return LZeroAnnotation(atToken.origin, qualifiedName, argumentList)

    }

    /**
     * Parses a possibly empty list of annotations.
     *
     * annotationList
     *   : annotation*
     *   ;
     */
    private fun parseAnnotationList(): LZeroAnnotationList {

        val annotations = mutableListOf<LZeroAnnotation>()

        while (input.hasLookAhead(AT)) {
            annotations.add(parseAnnotation())
        }

        return LZeroAnnotationList(annotations)

    }

    /**
     * Parses one argument.
     *
     * argument
     *   : argumentName? expression
     *   ;
     */
    private fun parseArgument(): LZeroArgument {

        val origin = input.lookAhead(1)!!.origin

        val argumentName = parseArgumentNameOpt()

        val expression = parseExpression()

        return LZeroArgument(origin, argumentName, expression)

    }

    /**
     * Parses an argument list.
     *
     * parameterList
     *   : "(" ( argument ("," argument)* )? ")"
     *   ;
     */
    private fun parseArgumentList(): LZeroArgumentList {

        val leftParenToken = input.read(LEFT_PARENTHESIS)

        val arguments = mutableListOf<LZeroArgument>()

        while (!input.consumeWhen(RIGHT_PARENTHESIS)) {
            arguments.add(parseArgument())
        }

        return LZeroSpecifiedArgumentList(leftParenToken.origin, arguments)

    }

    /**
     * Parses an optional argument list.
     */
    private fun parseArgumentListOpt(): LZeroArgumentList {

        if (input.hasLookAhead(LEFT_PARENTHESIS)) {
            return parseArgumentList()
        }

        return LZeroNullArgumentList

    }

    /**
     * Parses an optional argument name.
     *
     * argumentName
     *   : (simpleName "=")
     *   ;
     */
    private fun parseArgumentNameOpt(): LZeroArgumentName {

        if (input.hasLookAhead(2, EQ) && input.hasLookAhead(IDENTIFIER)) {

            val name = parseSimpleName()

            input.read(EQ)

            return LZeroSpecifiedArgumentName(name.origin, name.text)

        }

        return LZeroNullArgumentName

    }

    /**
     * Parses a hash tag and qualifiedName signifying a concept.
     *
     * concept
     *   : "#" qualifiedName
     *   ;
     */
    private fun parseConcept(): LZeroConcept {

        val hashToken = input.read(HASH)

        val qualifiedName = parseQualifiedName()

        return LZeroConcept(hashToken.origin, qualifiedName)

    }

    /**
     * Parses a connected element (to the right of a connector).
     *
     * connectedElement
     *   : qualifiedName
     *   | uuid
     *   | "[" element* "]"
     *   ;
     */
    private fun parseConnectedElement(): LZeroConnectedElement {

        if (input.hasLookAhead(IDENTIFIER)) {
            return LZeroConnectedQualifiedName(parseQualifiedName())
        }

        if (input.hasLookAhead(UUID)) {
            return LZeroConnectedUuid(parseUuid())
        }

        val leftBracketToken = input.read(LEFT_BRACKET)

        val elements = mutableListOf<LZeroElement>()

        while (!input.consumeWhen(RIGHT_BRACKET)) {
            elements.add(parseElement())
        }

        return LZeroConnectedElementList(leftBracketToken.origin, elements)

    }

    /**
     * Parses a list of connections (that can be empty).
     *
     * connectionList
     *   : implicitConnection+ explicitConnection* (containment | valueAssignment)?
     *   | explicitConnection+ (containment | valueAssignment)?
     *   | containment
     *   | valueAssignment
     *   | semicolonOrNewLine
     *   ;
     */
    private fun parseConnectionList(): LZeroConnectionList {

        val connections = mutableListOf<LZeroConnection>()

        while (input.hasLookAhead(COLON)) {
            connections.add(parseImplicitConnection())
        }

        while (input.hasLookAhead(TILDE)) {
            connections.add(parseExplicitConnection())
        }

        if (input.hasLookAhead(LEFT_BRACE)) {
            connections.add(parseContainment())
        }
        else if (input.hasLookAhead(EQ)) {
            connections.add(parseValueAssignment())
        }

        if (connections.isEmpty()) {
            parseSemicolonOrNewLine()
        }

        return LZeroConnectionList(connections)

    }

    /**
     * Parses a list of contained concepts.
     *
     * containment
     *   : "{" element* "}"
     *   ;
     */
    private fun parseContainment(): LZeroContainment {

        val leftBrace = input.read(LEFT_BRACE)

        val containedElements = mutableListOf<LZeroElement>()

        while (!input.hasLookAhead(RIGHT_BRACE)) {
            containedElements.add(parseElement())
        }

        return LZeroContainment(leftBrace.origin, containedElements)

    }

    /**
     * Parses a declaration element.
     *
     * declaration
     *   : documentation? annotationList concept qualifiedName? uuid? parameterList? connectionList
     *   ;
     */
    private fun parseDeclaration(): LZeroDeclaration {

        // documentation?
        val documentation = parseDocumentationOpt()

        // annotationList
        val annotations = parseAnnotationList()

        // concept
        val concept = parseConcept()

        // qualifiedName?
        val qualifiedName = parseQualifiedNameOpt()

        // uuid?
        val uuid = parseUuidOpt()

        // parameterList?
        val parameterList = parseParameterListOpt()

        // connectionList
        val connectionList = parseConnectionList()

        // Put together the declaration from its pieces.
        return LZeroDeclaration(
            documentation,
            annotations,
            concept,
            qualifiedName,
            uuid,
            parameterList,
            connectionList
        )

    }

    /**
     * Parses an optional block of documentation.
     */
    private fun parseDocumentationOpt(): LZeroDocumentation {

        if (input.hasLookAhead(DOCUMENTATION)) {
            return parseDocumentation()
        }

        return LZeroNullDocumentation

    }

    /**
     * Parses one block of documentation.
     *
     * documentation
     *   : DOCUMENTATION
     *   ;
     */
    private fun parseDocumentation(): LZeroBlockDocumentation {

        val token = input.read(DOCUMENTATION)
        return LZeroBlockDocumentation(token.origin, token.text)

    }

    /**
     * Parses an explicit connection (one where the connector is spelled out).
     *
     * explicitConnection
     *   : "~" qualifiedName parameterList? "~" connectedElement
     *   | "~" qualifiedName parameterList? "~>" connectedElement
     *   | "<~" qualifiedName parameterList? "~" connectedElement
     *   ;
     */
    private fun parseExplicitConnection(): LZeroExplicitConnection {

        val tildeToken1 = if (input.hasLookAhead(TILDE)) {
            input.read(TILDE)
        }
        else {
            input.read(LEFT_TILDE)
        }

        val qualifiedName = parseQualifiedName()

        val parameters = parseParameterListOpt()

        val tildeToken2 = if (input.hasLookAhead(TILDE)) {
            input.read(TILDE)
        }
        else {
            input.read(RIGHT_TILDE)
        }

        val direction = if (tildeToken1.type == LEFT_TILDE) {
            ELZeroConnectionDirection.LEFT
        }
        else if (tildeToken2.type == RIGHT_TILDE) {
            ELZeroConnectionDirection.RIGHT
        }
        else {
            ELZeroConnectionDirection.UNDIRECTED
        }

        val connector = LZeroConnector(qualifiedName.origin, qualifiedName, direction)

        return LZeroExplicitConnection(connector, parameters, parseConnectedElement())

    }

    /**
     * Parses an expression.
     *
     * expression
     *   : literalExpression
     *   | referenceExpression
     *   ;
     */
    private fun parseExpression(): LZeroExpression {

        if (
            input.hasLookAhead(BOOLEAN_LITERAL) ||
            input.hasLookAhead(CHARACTER_LITERAL) ||
            input.hasLookAhead(FLOATING_POINT_LITERAL) ||
            input.hasLookAhead(INTEGER_LITERAL) ||
            input.hasLookAhead(STRING_LITERAL) ||
            input.hasLookAhead(2, BOOLEAN_LITERAL) ||
            input.hasLookAhead(2, CHARACTER_LITERAL) ||
            input.hasLookAhead(2, FLOATING_POINT_LITERAL) ||
            input.hasLookAhead(2, INTEGER_LITERAL) ||
            input.hasLookAhead(2, STRING_LITERAL)
        ) {
            // literal Expression
            return parseLiteralExpression()
        }

        // referenceExpression
        return parseReferenceExpression()

    }

    /**
     * Parses an implicit connection (just a colon with connection type implied by the concept and connected).
     *
     * implicitConnection
     *   : ":" connectedElement
     *   ;
     */
    private fun parseImplicitConnection(): LZeroImplicitConnection {

        val colonToken = input.read(COLON)

        return LZeroImplicitConnection(colonToken.origin, parseConnectedElement())

    }

    /**
     * Parses a literal expression.
     *
     * literalExpression
     *   : documentation?
     *     ( BOOLEAN_LITERAL |
     *       CHARACTER_LITERAL |
     *       FLOATING_POINT_LITERAL |
     *       INTEGER_LITERAL |
     *       STRING_LITERAL )
     *   ;
     */
    private fun parseLiteralExpression(): LZeroLiteralExpression {

        val documentation = parseDocumentationOpt()

        val literalToken = input.readOneOf(
            BOOLEAN_LITERAL,
            CHARACTER_LITERAL,
            FLOATING_POINT_LITERAL,
            INTEGER_LITERAL,
            STRING_LITERAL
        )

        return when (literalToken.type) {
            BOOLEAN_LITERAL        -> LZeroBooleanLiteral(
                literalToken.origin,
                documentation,
                literalToken.text
            )
            CHARACTER_LITERAL      -> LZeroCharacterLiteral(
                literalToken.origin,
                documentation,
                literalToken.text
            )
            FLOATING_POINT_LITERAL -> LZeroFloatingPointLiteral(
                literalToken.origin,
                documentation,
                literalToken.text
            )
            INTEGER_LITERAL        -> LZeroIntegerLiteral(
                literalToken.origin,
                documentation,
                literalToken.text
            )
            STRING_LITERAL         -> LZeroStringLiteral(
                literalToken.origin,
                documentation,
                literalToken.text
            )
            else                   -> throw IllegalStateException("Unexpected token type: ${literalToken.type}.")
        }

    }

    /**
     * Parses one parameter.
     *
     * parameter
     *   : simpleName implicitConnection?
     *   ;
     */
    private fun parseParameter(): LZeroParameter {

        val simpleName = parseSimpleName()

        val connections = mutableListOf<LZeroConnection>()

        if (input.hasLookAhead(COLON)) {
            connections.add(parseImplicitConnection())
        }
        else {
            parseSemicolonOrNewLine()
        }

        return LZeroParameter(simpleName.origin, simpleName)

    }

    /**
     * Parses a parameter list.
     *
     * parameterList
     *   : "(" ( parameter ("," parameter)* )? ")"
     *   ;
     */
    private fun parseParameterList(): LZeroParameterList {

        val leftParenToken = input.read(LEFT_PARENTHESIS)

        val arguments = mutableListOf<LZeroParameter>()

        while (!input.consumeWhen(RIGHT_PARENTHESIS)) {
            arguments.add(parseParameter())
        }

        return LZeroSpecifiedParameterList(leftParenToken.origin, arguments)

    }

    /**
     * Parses an optional parameter list.
     */
    private fun parseParameterListOpt(): LZeroParameterList {

        if (input.hasLookAhead(LEFT_PARENTHESIS)) {
            return parseParameterList()
        }

        return LZeroNullParameterList

    }

    /**
     * Parses an optional qualified name.
     */
    private fun parseQualifiedNameOpt(): LZeroName {

        if (!input.hasLookAhead(IDENTIFIER)) {
            return LZeroNullName
        }

        return parseQualifiedName()
    }

    /**
     * Parses a qualified name.
     *
     * qualifiedName
     *   : simpleName ("." simpleName)*
     *   ;
     */
    private fun parseQualifiedName(): LZeroName {

        val names = mutableListOf<LZeroSimpleName>()

        while (true) {

            val token = input.read(IDENTIFIER)
            names.add(LZeroSimpleName(token.origin, token.text))

            if (!input.hasLookAhead(2, IDENTIFIER) || !input.consumeWhen(DOT)) {
                break
            }

        }

        if (names.size > 1) {
            return LZeroQualifiedName(names)
        }

        return names[0]

    }

    /**
     * Parses a reference expression.
     *
     * referenceExpression
     *   : documentation? (qualifiedName | uuid) parameterList? valueAssignment?
     *   ;
     */
    private fun parseReferenceExpression(): LZeroReferenceExpression {

        val documentation = parseDocumentationOpt()

        val qualifiedName = parseQualifiedNameOpt()

        val uuid = if (qualifiedName is LZeroNullName) parseUuid() else LZeroNullUuid

        val argumentList = parseArgumentListOpt()

        val valueAssignment = parseValueAssignmentOpt()

        return LZeroReferenceExpression(documentation, qualifiedName, uuid, argumentList, valueAssignment)

    }

    /**
     * Parses a semicolon or line break.
     *
     * semicolonOrNewLine
     *   : ";"
     *   | /* next token occurs on new line */
     *   ;
     */
    private fun parseSemicolonOrNewLine() {

        if (input.hasLookAheadOnNewLine()) {
            return
        }

        input.read(SEMICOLON)
    }

    /**
     * Parses a simple name (single identifier).
     *
     * simpleName
     *   : IDENTIFIER
     *   ;
     */
    private fun parseSimpleName(): LZeroSimpleName {

        val nameToken = input.read(IDENTIFIER)

        return LZeroSimpleName(nameToken.origin, nameToken.text)

    }

    /**
     * Parses an optional UUID.
     *
     * uuid
     *   : UUID
     *   ;
     */
    private fun parseUuid(): LZeroKnownUuid {

        val uuidToken = input.read(UUID)

        return LZeroKnownUuid(uuidToken.origin, uuidToken.text)

    }

    /**
     * Parses an optional UUID.
     */
    private fun parseUuidOpt(): LZeroUuid {

        if (!input.hasLookAhead(UUID)) {
            return LZeroNullUuid
        }

        return parseUuid()

    }

    /**
     * Parses a value assignment.
     *
     * valueAssignment
     *   : "=" literalExpression
     *   ;
     */
    private fun parseValueAssignment(): LZeroValueAssignment {

        val equals = input.read(EQ)

        // TODO: parse an arbitrary expression or just a literal?
        val expression = parseLiteralExpression()

        return LZeroLiteralValueAssignment(equals.origin, expression)

    }

    /**
     * Parses an optional value assignment.
     */
    private fun parseValueAssignmentOpt(): LZeroValueAssignment {

        if (input.hasLookAhead(EQ)) {
            return parseValueAssignment()
        }

        return LZeroNullValueAssignment

    }

    ////

    /**
     * Adds the file name to a token origin.
     */
    private val LZeroToken.origin
        get() = LZeroFileOrigin(fileName, this.line, this.column)

}

//---------------------------------------------------------------------------------------------------------------------

