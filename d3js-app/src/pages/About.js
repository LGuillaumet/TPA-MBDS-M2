import { Container, Row, Col, Card } from 'reactstrap';

import { NavBar } from '../components/NavBar';


export const About = () => {
	return (
		<>
			<NavBar/>
			<Container>
				<Row className="justify-content-center">
					<Col xs="12" sm="12" md="12" lg="12" xl="12">
						<Card className="home-card">
							<Row className="justify-content-center">
								<Col xs="12" sm="12" md="12" lg="12" xl="12">
								</Col>
							</Row>
						</Card>
					</Col>
				</Row>
			</Container>

		</>
	);
}
