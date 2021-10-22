(ns mynote-api.sql.notes
(:require [hugsql.core :as hugsql]))
(hugsql/def-db-fns "mynote_api/sql/notes.sql")