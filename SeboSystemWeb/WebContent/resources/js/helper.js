/**
 * Initialize row selection at a table
 */
function initSelectableRows(formId, btnView) {
	jQuery(function(e) {
		jQuery(formId + " table tbody tr td").click(
				function(e) {
					if (jQuery(this).find('.btn-group').length == 0)
						location.href = jQuery(this).parent().find(btnView)
								.attr('href');
				});
	});
}

/**
 * Open the param dialog when the request returns 'sucess'
 */
function openDialogOnSucess(event, dialogId) {
	if (event.status === 'success') {
		jQuery(dialogId).modal();
	}
}
