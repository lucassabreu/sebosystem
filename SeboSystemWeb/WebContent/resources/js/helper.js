Application = function() {
};

Application.prototype = {
    _modalQuee : [],
    _lastHash : null,
    stopReturning : false,

    // functions

    /**
     * Initialize the application
     */
    init : function(event) {
        var that = this;

        this.changeHash("");
        jQuery(window).on('hashchange', function(e) {
            that.onLocationHashChange(e);
        });
    },

    /**
     * Retrieves the modal element related with the param
     * 
     * @param modalId
     * @returns
     */
    getModal : function(modalId) {

        var el = this.getElById(modalId);

        if (el.hasClass('modal'))
            return el;

        return el.find('.modal:first');
    },

    /**
     * Initialize the top modal element when the event status became 'success'
     * 
     * @param event
     */
    initModalOnSuccess : function(event) {
        if (event.status === 'success') {
            this.initModal(this.topModalOnQuee().modal);
        }
    },

    /**
     * Initilize the components at a modal
     * 
     * @param modalEl
     */
    initModal : function(modalEl) {
        var that = this, historyBack = function(e) {
            // once the onLocationHash was listening to the url changes, than
            // call the history.back will dispath the event and call it

            if (!that.stopReturning) {
                that.stopReturning = true;
                window.history.back();
            } else
                that.stopReturning = false;

            if (e.type == "click")
                e.preventDefault();
        };

        if (modalEl.attr('data-modal-processed') != "true") {
            modalEl.attr('data-modal-processed', "true");
            modalEl.on('hide.bs.modal', historyBack);
        }

        modalEl.find("[data-modal-action=goBack]").click(historyBack).removeAttr('data-modal-action');
    },

    /**
     * Open the param modal
     * 
     * @param modalId
     *            Can be a jQuery object, or a id
     */
    openModal : function(modalId, extraId, notAddHistory) {
        var el = null;

        if (modalId.modal) {

            el = modalId.modal;
            extraId = modalId.extraId;
            modalId = modalId.hash;

        } else {
            el = this.getModal(modalId);
            if (!extraId || extraId == null)
                extraId = "";
            else
                extraId = "_".concat(extraId);

            this.initModal(el);
        }

        if (!notAddHistory) {
            this.changeHash(modalId.concat(extraId));
        }

        if (this.topModalOnQuee() != null) {
            this.stopReturning = true;
            this.topModalOnQuee().modal.modal('hide');
        }

        el.modal({
            keyboard : true, // allow ESCAPE button to exit
        });

        this.addModalToQuee(modalId, el, extraId);
    },

    /**
     * Open the param modal when the request returns 'sucess'
     */
    openModalOnSuccess : function(event, modalId, extraId) {
        if (event.status === 'success') {
            this.openModal(modalId, extraId);
        }
    },

    closeModal : function(modalId) {
        var modal = this.getModal(modalId);

        if (modal.hasClass('in')) {
            this.stopReturning = true;
            modal.modal("hide");
        }

        this.popModalQuee();
        var topModalHistory = this.popModalQuee();

        if (topModalHistory !== null)
            this.openModal(topModalHistory, null, true);
    },

    /**
     * Close a modal if was no messages returned
     */
    closeModalOnSuccess : function(event, modalId, messagesId, updateElements) {
        app.initModalOnSuccess(event);
        
        if (event.status === 'success') {
            var messages = this.getElById(messagesId);

            if (messages.find('*').length === 0) {
                this.clearModalQuee();
                this.closeModal(modalId);
            }
        }
    },

    /**
     * Add a modal to the quee
     * 
     * @param modalId
     */
    addModalToQuee : function(modalId, el, hash) {
        this._modalQuee.push(new Application.ModalHistory(modalId, el, hash));
    },

    topModalOnQuee : function() {
        if (this._modalQuee.length > 0) {
            return this._modalQuee[this._modalQuee.length - 1];
        } else
            return null;
    },

    /**
     * Remove the last modal from the quee
     */
    popModalQuee : function() {
        if (this._modalQuee.length > 0) {
            return this._modalQuee.pop();
        } else
            return null;
    },

    /**
     * Clean the modal quee
     */
    clearModalQuee : function() {
        while (this._modalQuee.length > 0)
            this._modalQuee.pop();
    },

    /**
     * Function called every time the location hash change
     * 
     * @param event
     */
    onLocationHashChange : function(event) {

        if (window.location.hash == this._lastHash)
            return;

        var lasModal = this.topModalOnQuee();

        if (lasModal != null) {
            this.closeModal(lasModal.modal);
        }
    },

    /**
     * Change the URL location to include the anchor passed by param
     * 
     * @param anchorName
     */
    changeHash : function(anchorName) {
        this._lastHash = '#' + anchorName;
        window.location.href = window.location.pathname + window.location.search + this._lastHash;
    },

    /**
     * Reaload a container with cascating grid layout
     */
    reloadCascatingGrid : function(query) {
        jQuery(query).masonry();
    },

    /**
     * Initialize the list or table row to launch a context menu, when tap hold
     * event or right button click
     * 
     * @param formId
     */
    initContextMenuControl : function(queryElements) {

        jQuery(queryElements).on("taphold", function(e) {
            alert("Holded");
        });

    },

    /**
     * Initialize row selection at a table
     */
    initSelectableRows : function(formId, btnView, click) {
        var that = this;

        jQuery(function(e) {
            jQuery(formId + " table tbody tr td").click(function(e) {
                if (!jQuery(e.target).parent().hasClass('btn-group-actions') && !jQuery(e.target).hasClass('btn-group-actions')) {
                    if (click === true)
                        jQuery(this).parent().find(btnView).click();
                    else
                        location.href = jQuery(this).parent().find(btnView).attr('href');
                }
            });

            that.initContextMenuControl(formId + " table tbody tr td");
        });
    },

    /**
     * Initialize element selection at a list
     */
    initSelectableElements : function(queryElements, btnView, click) {
        var that = this;

        jQuery(function(e) {
            jQuery(queryElements).click(function(e) {
                if (!jQuery(e.target).parent().hasClass('btn-group-actions') && !jQuery(e.target).hasClass('btn-group-actions')) {
                    if (click === true)
                        jQuery(this).parent().find(btnView).click();
                    else
                        location.href = jQuery(this).parent().find(btnView).attr('href');
                }
            });

            that.initContextMenuControl(queryElements);
        });
    },

    /**
     * Find a element by d
     */
    getElById : function(id) {
        if (id.jquery)
            return id;

        return jQuery("[id='" + id + "']");
    }
};

Application.ModalHistory = function(hash, modal, extraId) {
    this.modal = modal;
    this.hash = hash;
    this.extraId = extraId;
};

var app = new Application();

jQuery(function(event) {
    app.init(event);
});
