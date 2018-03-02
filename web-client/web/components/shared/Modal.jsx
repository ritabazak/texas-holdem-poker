class Modal extends React.Component {
    modalDiv;

    constructor(props) {
        super(props);
    }

    show() {
        if (this.modalDiv) {
            $(this.modalDiv).modal({show: true});
        }
    }

    close() {
        if (this.modalDiv) {
            $(this.modalDiv).modal({show: false});
        }
    }

    componentDidMount() {
        $(this.modalDiv).modal({backdrop: 'static', show: false});
    }

    render() {
        let {title, onOK, hideDismiss} = this.props;

        return (
            <div className="modal fade" role="dialog" ref={d => this.modalDiv = d}>
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h4 className="modal-title">{title}</h4>

                            {!hideDismiss && (
                                <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            )}
                        </div>

                        <div className="modal-body">
                            {this.props.children}
                        </div>

                        <div className="modal-footer">
                            {!hideDismiss && (
                                <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
                            )}
                            <button type="button" className="btn btn-primary" data-dismiss="modal"
                                    onClick={onOK}>OK</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}