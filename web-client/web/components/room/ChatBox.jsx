class ChatBox extends React.Component {
    chatList;

    constructor(props) {
        super(props);

        this.state = {
            message: ''
        };

        this.submitMessage = this.submitMessage.bind(this);
    }

    componentDidUpdate() {
        if (this.chatList) {
            this.chatList.scrollTop = this.chatList.scrollHeight - this.chatList.clientHeight;
        }
    }

    submitMessage(event) {
        event.preventDefault();

        let {onMessage} = this.props;
        let {message} = this.state;

        if (onMessage) {
            onMessage(message);
            this.setState({message: ''});
        }
    }

    render() {
        let {chat} = this.props;

        return (
            <div className="chat-box-wrapper">
                <div className="chat-box flex-column">
                    <div className="flex chat-list" ref={d => this.chatList = d}>
                        <div className="chat-message">
                            <span style={{'text-decoration': 'underline'}}>Bot:</span>
                            &nbsp;
                            <span>Welcome to the machine</span>
                        </div>

                        {chat.map(c => (
                            <div className="chat-message">
                                <span style={{'text-decoration': 'underline'}}>{c.author}:</span>
                                &nbsp;
                                <span>{c.message}</span>
                            </div>
                        ))}
                    </div>

                    <form onSubmit={this.submitMessage}>
                        <input type="text" className="form-control"
                               value={this.state.message}
                               placeholder="Chat..."
                               onChange={e => this.setState({message: e.target.value})} />
                    </form>
                </div>
            </div>
        );
    }
}