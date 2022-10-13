---
# Remember, leave a key empty if there is no value.  None will be a string,
# not a Python "NoneType"
#
# Also remember that all examples have 'disable_action' set to True.  If you
# want to use this action as a template, be sure to set this to False after
# copying it.
actions:
  1:
    action: delete_indices
    description: >-
      Delete indices older than ${CURATOR_PERIOD} days (based on index name).
      Ignore the error if the filter does not result in an actionable list
      of indices (ignore_empty_list) and exit cleanly.
    options:
      ignore_empty_list: True
      disable_action: False
    filters:
    - filtertype: age
      source: name
      direction: older
      timestring: '%Y.%m.%d'
      unit: days
      unit_count: ${CURATOR_PERIOD}
  2:
    action: index_settings
    description: "Remove read-only setting of today's indices, if present"
    options:
      index_settings:
        index:
          blocks:
            read_only_allow_delete: False
      ignore_unavailable: False
      preserve_existing: False
    filters:
    - filtertype: age
      source: name
      direction: younger
      timestring: '%Y.%m.%d'
      unit: days
      unit_count: 1
  3:
    action: index_settings
    description: "Remove read-only setting of .kibana index, if present"
    options:
      index_settings:
        index:
          blocks:
            read_only_allow_delete: False
      ignore_unavailable: False
      preserve_existing: False
    filters:
    - filtertype: pattern
      kind: prefix
      value: '.kibana'
