import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import CssBaseline from "@material-ui/core/CssBaseline";
import Container from "@material-ui/core/Container";
import Paper from "@material-ui/core/Paper";
import TextField from '@material-ui/core/TextField';
import Grid from "@material-ui/core/Grid";
import Divider from "@material-ui/core/Divider";
import {useDropzone} from "react-dropzone";
import RootRef from "@material-ui/core/RootRef";
import {makeStyles} from "@material-ui/core/styles";
import {green} from "@material-ui/core/colors";
import Button from "@material-ui/core/Button";
import clsx from "clsx";
import Alert from '@material-ui/lab/Alert';
import AlertTitle from '@mui/material/AlertTitle';
import Box from '@material-ui/core/Box';

import SaveIcon from '@material-ui/icons/Save';
import FormGroup from '@material-ui/core/FormGroup'


const useStyles = makeStyles((theme) => ({
    root: {
        '& > *': {
            margin: theme.spacing(1),
        },
    },
    margin: {
        margin: theme.spacing(1),
    },
    extendedIcon: {
        marginRight: theme.spacing(1),
    },
    dropzoneContainer: {
        height: 300,
        background: "#efefef",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        borderStyle: "dashed",
        borderColor: "#aaa",
    },
    preview: {
        width: 250,
        height: 250,
        margin: "auto",
        display: "block",
        marginBottom: theme.spacing(2),
        objectFit: "contain",
    },
    wrapper: {
        margin: theme.spacing(1),
        position: "relative",
    },
    buttonSuccess: {
        backgroundColor: green[500],
        "&:hover": {
            backgroundColor: green[700],
        },
    },
    fabProgress: {
        color: green[500],
        position: "absolute",
        top: -6,
        left: -6,
        zIndex: 1,
    },
    buttonProgress: {
        color: green[500],
        position: "absolute",
        top: "50%",
        left: "50%",
        marginTop: -12,
        marginLeft: -12,
    },
}));

function App() {
    const classes = useStyles();
    const [value, setValue] = React.useState("");
    const [message, setMessage] = React.useState("");
    const [isMessage, setIsMessage] = React.useState(false)
    const [success, setSuccess] = React.useState(false);
    const [file, setFile] = React.useState();
    const [preview, setPreview] = React.useState();
    const [isError, setIsError] = React.useState(false)
    const [errorMessages, setErrorMessages] = React.useState([])
    const [errorServiceMessage, setErrorServiceMessage] = React.useState();
    const [isErrorService, setIsErrorService] = React.useState(false)


    const handleChange = e => {
        setIsError(false)
        console.log('Typed=>' + e.target.value);
        setValue(e.target.value)

    };
    const onDrop = React.useCallback((acceptedFiles) => {
        setIsError(false)
        const fileDropped = acceptedFiles[0];
        setFile(fileDropped);
        const previewUrl = URL.createObjectURL(fileDropped);
        setPreview(previewUrl);
        setSuccess(false);
    });

    const buttonClassname = clsx({
        [classes.buttonSuccess]: success,
    });

    const {getRootProps, getInputProps} = useDropzone({
        multiple: false,
        onDrop,
    });
    const {ref, ...rootProps} = getRootProps();

    const train = async () => {
            setIsError(false)
            setIsMessage(false)
            setIsErrorService(false)
            let errorList = []
            if (!value) {
                errorList.push("Value field is empty")
            }
            if (isNaN(+value)) {
                errorList.push("Value should be a number")
            }
            if (!file) {
                errorList.push("File field is empty")
            }
            if (errorList.length < 1) {
                setSuccess(false);
                const formData = new FormData();
                formData.append("file", file);
                formData.append("label", value)
                fetch('/api/fileUpload/train', {
                    method: 'POST',
                    body: formData
                }).then((res) => {
                    if (res.ok) {
                        setMessage(`Neural network was trained successfully`);
                        setIsMessage(true)
                        return res;
                    } else {
                        res.json().then((message) => {
                            console.log(message);
                            setIsErrorService(true);
                            setErrorServiceMessage(message);

                        });
                    }
                });
            } else {
                setErrorMessages(errorList)
                setIsError(true)
            }
        }
    ;
    const predict = async () => {
            try {
                setIsError(false)
                setIsMessage(false)
                setIsErrorService(false)
                let errorList = []
                if (!file) {
                    errorList.push("File field is empty")
                }

                if (errorList.length < 1) {
                    setSuccess(false);
                    const formData = new FormData();
                    formData.append("file", file);
                    const API_URL = "http://localhost:8080/api/fileUpload/predict";
                    fetch('/api/fileUpload/predict', {
                        method: 'POST',
                        body: formData
                    }).then((res) => {
                        if (res.ok) {
                            res.json().then((res) => {
                                const {result, probability} = res;
                                setMessage(`The result is ${result} with probability ${probability}`);
                                setIsMessage(true)
                            })
                        } else {
                            res.json().then((message) => {
                                console.log(message);
                                setIsErrorService(true);
                                setErrorServiceMessage(message);

                            })
                        }
                    });
                } else {
                    setErrorMessages(errorList)
                    setIsError(true)
                }
            } catch
                (err) {
                alert(err.message);
            }
        }
    ;
    const saveNeuralNetwork = async () => {
        try {
            setIsError(false)
            setSuccess(false);
            fetch('/api/save', {
                method: 'GET',
            }).then((res) => {
                if (res.ok) {
                    setSuccess(true);
                    setMessage(`New version of neural Network saved successfully`);
                    setIsMessage(true)
                    return res;
                } else {
                    res.json().then((message) => {
                        console.log(message);
                        setIsErrorService(true);
                        setErrorServiceMessage(message);
                    })
                }
            });
        } catch (err) {
            alert(err.message);
        }
    };

    return (
        <>
            <CssBaseline/>
            <AppBar position="fixed">
                <Toolbar>
                    <Typography variant="h6">Digit prediction using neural network</Typography>
                </Toolbar>
            </AppBar>
            <Toolbar/>
            <Toolbar/>
            <Container maxWidth="md">
                <Paper elevation={4}>
                    <Grid container>
                        <Grid item xs={12}>
                            <Typography align="center" style={{padding: 16}}>
                                File Upload
                            </Typography>
                            <Divider/>
                        </Grid>
                        <Grid item xs={6} style={{padding: 16}}>
                            <RootRef rootRef={ref}>
                                <Paper elevation={0} {...rootProps} className={classes.dropzoneContainer}>
                                    <input {...getInputProps()} />
                                    <p>Drag 'n' drop file with number here, or click to select files</p>
                                </Paper>
                            </RootRef>
                            {isErrorService && <>
                                <Box pt={1}>
                                    <Alert severity="error">
                                        <AlertTitle>Error</AlertTitle>
                                        {errorServiceMessage}
                                    </Alert>
                                </Box>

                            </>}
                            {isMessage && <>
                                <Box pt={1}>
                                    <Alert onClose={() => {
                                        setIsMessage(false)
                                    }} severity="success">
                                        <AlertTitle>Success</AlertTitle>
                                        {message}
                                    </Alert>
                                </Box>
                            </>}
                        </Grid>

                        <Grid item xs={6} style={{padding: 16}}>
                            <Typography align="center" variant="subtitle1">
                                Preview
                            </Typography>
                            <img
                                onload={() => URL.revokeObjectURL(preview)}
                                className={classes.preview}
                                src={preview || "https://via.placeholder.com/250"}
                            />
                            <Divider/>
                            {file && <>
                                <Grid
                                    container
                                    style={{marginTop: 16}}
                                    alignItems="center"
                                    spacing={3}
                                >
                                </Grid>
                            </>}
                            <div>
                                {isError &&
                                <Box pb={1}>
                                    <Alert severity="error">
                                        {errorMessages.map((msg, i) => {
                                            return <div key={i}>{msg}</div>
                                        })}
                                    </Alert>
                                </Box>
                                }
                            </div>

                            <FormGroup>
                                <TextField id="outlined-basic" value={value} onChange={handleChange} label="Number"
                                           variant="outlined"
                                           style={{backgroundColor: '#E5E5E8'}}/>
                                <div style={{fontSize: 12, color: "red"}}></div>
                            </FormGroup>

                            <div className={classes.root}>
                                <Button
                                    variant="contained"
                                    color="secondary"
                                    size="large"
                                    onClick={predict}>
                                    Predict
                                </Button>
                                <Button
                                    variant="contained"
                                    color="primary"
                                    size="large"
                                    onClick={train}>
                                    Train
                                </Button>
                                <Button
                                    variant="contained"
                                    color="primary"
                                    size="large"
                                    className={classes.button}
                                    startIcon={<SaveIcon/>}
                                    onClick={saveNeuralNetwork}>
                                    Save
                                </Button>
                            </div>


                        </Grid>

                    </Grid>

                </Paper>

            </Container>

        </>
    );
}

export default App;
