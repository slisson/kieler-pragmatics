package de.cau.cs.kieler.kex.controller.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import de.cau.cs.kieler.core.KielerException;
import de.cau.cs.kieler.core.KielerModelException;
import de.cau.cs.kieler.kex.controller.ErrorMessage;
import de.cau.cs.kieler.kex.model.Example;
import de.cau.cs.kieler.kex.model.ExampleResource;

public class ExampleImport {

	private final static String workspaceLocation = Platform.getLocation()
			.toString();

	private final static String standardPicPath = "files/noPreview.png";
	private final static String kexNamespaceId = "de.cau.cs.kieler.kex";

	/**
	 * @param selectedResource
	 * @param selectedExamples
	 * @throws KielerException
	 */
	public static List<String> importExamples(final IPath selectedResource,
			final List<Example> selectedExamples, boolean isQuickStart,
			boolean checkDuplicate) throws KielerException {
		if (isQuickStart) {
			return createQuickStartProject();
		}

		List<String> directOpens = new ArrayList<String>();

		StringBuilder destFolder = new StringBuilder();
		destFolder
				.append(workspaceLocation)
				.append((selectedResource != null ? selectedResource.toString()
						: "")).append("/");

		for (Example example : selectedExamples) {

			List<ExampleResource> resources = example.getResources();

			String rootDirectory = example.getRootDir();
			int exampleBeginIndex = 0;
			if (rootDirectory != null && rootDirectory.length() > 1) {
				exampleBeginIndex = rootDirectory.length();
			}

			handleResources(directOpens, resources, destFolder.toString(),
					example.getNamespaceId(), exampleBeginIndex, checkDuplicate);
		}
		return directOpens;
	}

	private static List<String> createQuickStartProject() {
		return null;
	}

	private static void handleResources(List<String> directOpens,
			List<ExampleResource> resources, String destFolder,
			String nameSpaceId, int exampleBeginIndex, boolean checkDuplicate)
			throws KielerException {
		Bundle bundle = Platform.getBundle(nameSpaceId);

		for (ExampleResource resource : resources) {
			try {
				String localPath = resource.getLocalPath();
				String destPath = localPath.substring(exampleBeginIndex);

				switch (resource.getResourceType()) {
				case PROJECT:
					// creates a new project
					IProgressMonitor progressMonitor = new NullProgressMonitor();
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
							.getRoot();
					IProject project = root.getProject(destPath);
					project.create(progressMonitor);
					project.open(progressMonitor);
					break;

				case FOLDER:
					File destFile = new File(destFolder + "/" + destPath);
					if (destFile.exists()) {
						throw new KielerModelException(
								ErrorMessage.DUPLICATE_EXAMPLE,
								destFile.getName());
					}
					IOHandler.createFolder(destFolder + "/" + destPath);
					break;

				case FILE:
					URL entry = bundle.getEntry(localPath);
					IOHandler.writeFile(entry, destFolder + destPath,
							checkDuplicate);
					if (resource.isDirectOpen())
						directOpens.add(destFolder + destPath);
					break;

				}
			} catch (FileNotFoundException e) {
				throw new KielerException(ErrorMessage.NO_Import, e);
			} catch (IOException e1) {
				throw new KielerException(ErrorMessage.NO_Import, e1);
			} catch (CoreException e2) {
				throw new KielerException(ErrorMessage.NO_Import, e2);
			}
		}
	}

	public static InputStream loadPreviewPic(Example example)
			throws KielerException {
		Bundle bundle = Platform.getBundle(example.getNamespaceId());
		URL entry = bundle.getEntry(example.getPreviewPicPath());
		try {
			return entry.openStream();
		} catch (IOException e) {
			throw new KielerException(ErrorMessage.PREVIEW_LOAD_ERROR
					+ example.getTitle());
		}
	}

	public static InputStream loadStandardPic() {
		Bundle bundle = Platform.getBundle(ExampleImport.kexNamespaceId);
		URL entry = bundle.getEntry(ExampleImport.standardPicPath);
		try {
			return entry.openStream();
		} catch (IOException e) {
			// should not happen at runtime
			e.printStackTrace();
		}
		return null;
	}

	public static void validate(IPath selectedResource,
			List<Example> selectedExamples, boolean checkDuplicate)
			throws KielerException {
		if (selectedExamples == null || selectedExamples.size() == 0) {
			throw new KielerException(ErrorMessage.NO_EXAMPLE_SELECTED);
		}

		if (checkDuplicate) {
			for (Example example : selectedExamples) {
				if (checkDuplicate(selectedResource, example.getResources(),
						example.getTitle())) {
					throw new KielerModelException(
							ErrorMessage.DUPLICATE_EXAMPLE, example);
				}
			}
		}
	}

	private static boolean checkDuplicate(IPath target,
			List<ExampleResource> resources, String exampleTitle) {
		// TODO duplikat check bauen
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// for (ExampleResource exampleResource : resources) {
		// target.toPortableString() +
		//
		// String localPath = exampleResource.getLocalPath();
		// String checkPath = target.toPortableString() + File.separatorChar +
		// localPath;
		//
		//
		// IProject project = root.getF(target);
		// IFolder folder = project.getFolder(exampleTitle);
		// // assert folder.exists();
		// IFile file = folder.getFile("myfile");
		// assert !file.exists();
		// file.create(stream, false, monitor);
		// assert file.exists();
		return false;
	}
}
