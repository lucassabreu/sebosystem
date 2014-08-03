/**
 * Initialize row selection at a table
 */
function initSelectableRows(formId, btnView) {
    jQuery(function(e) {
        jQuery(formId + " table tbody tr td").click(function(e) {
            if (jQuery(this).find('.btn-group').length == 0)
                location.href = jQuery(this).parent().find(btnView).attr('href');
        });
    });
}

/**
 * Open the param dialog when the request returns 'sucess'
 */
function openDialogOnSuccess(event, dialogId) {
    console.log("hover here?");
    if (event.status === 'success') {
        jQueryJSF(dialogId).modal();
    }
}

/**
 * Find a element by its complex JSF id
 */
function jQueryJSF(id) {
    return jQuery("[id='" + id + "']");
}

function initSelectionGrid(tableId, selectElement) {
    jQueryJSF(tableId).find('tr').click(function(e) {
        jQuery(this).find(".check-select").trigger('change');

        jQuery(this).parent().find('tr').removeClass('selected-row');
        jQuery(this).addClass('selected-row');

        jQuery(this).parent().find(".check-select-view").addClass('glyphicon-unchecked').removeClass('glyphicon-check');
        jQuery(this).find(".check-select-view").addClass('glyphicon-check').removeClass('glyphicon-unchecked');
    });
}
