package io.github.soat7.videomanager.database.r2dbc.statements

object VideoSqlStatements {

    private const val TABLE_NAME = "video_manager.video"

    private const val ALL_FIELDS = """
        id,
        user_id,
        name,
        status,
        created_at,
        updated_at,
        metadata,
        input_path,
        output_path
    """

    private const val ALL_FIELDS_WITH_BINDS = """
        :id,
        :user_id,
        :name,
        :status,
        :created_at,
        :updated_at,
        :metadata,
        :input_path,
        :output_path
    """

    const val INSERT = """
        INSERT INTO $TABLE_NAME(
            $ALL_FIELDS
        ) VALUES (
            $ALL_FIELDS_WITH_BINDS
        )
    """

    const val UPDATE = """
        UPDATE $TABLE_NAME
        SET
            status = :status,
            updated_at = :updated_at,
            metadata = :metadata,
            output_path = :output_path
        WHERE id = :id
    """

    const val FIND_BY_USER_ID = """
        SELECT $ALL_FIELDS
        FROM $TABLE_NAME
        WHERE user_id = :user_id
    """

    const val FIND_BY_ID = """
        SELECT $ALL_FIELDS
        FROM $TABLE_NAME
        WHERE id = :id
    """
}
