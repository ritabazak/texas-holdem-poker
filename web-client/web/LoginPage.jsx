class LoginPage extends React.Component {
    static ERRORS = {
        'NAME_TAKEN': 'The name you have chosen is already in use.',
        'NAME_EMPTY': 'Please enter a name.'
    };

    constructor(props) {
        super(props);

        this.state = {
            name: '',
            type: 'human'
        };

        this.playerTypeSelect = this.playerTypeSelect.bind(this);
        this.submitForm = this.submitForm.bind(this);
    }

    playerTypeSelect(e) {
        this.setState({type: e.target.value});
    }

    submitForm(e) {
        e.preventDefault();

        fetch('api/login', {
            method: 'POST',
            body: JSON.stringify(this.state),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        })
            .then(res => res.json())
            .then(resJson => {
                if (resJson.success) {
                    window.location.href = 'lobby.html'; //TODO
                }
                else {
                    throw new Error(resJson.error);
                }
            })
            .catch(err => {
                this.setState({
                    name: ''
                });
                toastr.error(LoginPage.ERRORS[err.message]);
            });
    }

    render() {
        return (
            <div className="container">
                <h1 className="text-center">Welcome to the best Poker game ever!</h1>

                <form onSubmit={this.submitForm}>
                    <div className="form-group">
                        <label>Name:</label>
                        <input type="text" className="form-control" value={this.state.name}
                               onChange={e => this.setState({name: e.target.value})} />
                    </div>

                    <div className="form-group">
                        <div className="form-check">
                            <label className="form-check-label">
                                <input className="form-check-input" type="radio"
                                       name="playerType" value="human" checked={this.state.type === 'human'}
                                       onChange={this.playerTypeSelect}/>
                                Human
                            </label>
                        </div>

                        <div className="form-check">
                            <label className="form-check-label">
                                <input className="form-check-input" type="radio"
                                       name="playerType" value="computer" checked={this.state.type === 'computer'}
                                       onChange={this.playerTypeSelect} />
                                Computer
                            </label>
                        </div>
                    </div>

                    <button type="submit" className="btn btn-primary">Submit</button>
                </form>
            </div>

        );
    }
}